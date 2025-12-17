package main

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
	"strings"

	"github.com/go-chi/chi/v5"
)

/* =========================
   MODELS
========================= */

type Crud struct {
	Id          int    `json:"id"`
	Name        string `json:"name"`
	FatherName  string `json:"fatherName"`
	Age         int    `json:"age"`
	DateOfBirth string `json:"dateOfBirth"`
	Designation string `json:"designation"`
	Email       string `json:"email"`
	Username    string `json:"username"`
}

/* =========================
   HELPERS
========================= */

func getUser(username string) (int, string, error) {
	var id int
	var role string

	err := DB.QueryRow(
		`SELECT id, role FROM login_users WHERE LOWER(name)=LOWER(@p1)`,
		username,
	).Scan(&id, &role)

	// 🔥 DEBUG LOG - REMOVE AFTER TESTING
	log.Printf("DEBUG getUser: username=%s, id=%d, role='%s', err=%v", username, id, role, err)

	return id, role, err
}

func canModify(recordId string, username string) bool {
	userId, role, err := getUser(username)
	if err != nil {
		log.Printf("ERROR canModify: getUser failed for username=%s, err=%v", username, err)
		return false
	}

	// 🔥 FIX: Normalize role to uppercase and trim whitespace
	role = strings.ToUpper(strings.TrimSpace(role))

	// 🔥 DEBUG LOG
	log.Printf("DEBUG canModify: recordId=%s, username=%s, userId=%d, role='%s'", recordId, username, userId, role)

	// 🔥 ADMIN → FULL ACCESS
	if role == "ADMIN" {
		log.Printf("DEBUG canModify: ADMIN access granted")
		return true
	}

	// 🔒 USER → MUST OWN RECORD
	var ownerId int
	err = DB.QueryRow(
		`SELECT user_id FROM crud_items WHERE id=@p1`,
		recordId,
	).Scan(&ownerId)

	if err != nil {
		log.Printf("ERROR canModify: record lookup failed for id=%s, err=%v", recordId, err)
		return false
	}

	// 🔥 DEBUG LOG
	log.Printf("DEBUG canModify: ownerId=%d, userId=%d, match=%v", ownerId, userId, ownerId == userId)

	return ownerId == userId
}

/* =========================
   GET (ADMIN = ALL, USER = OWN)
========================= */

func GetCrud(w http.ResponseWriter, r *http.Request) {
	username := r.URL.Query().Get("username")
	if username == "" {
		http.Error(w, "Username required", 400)
		return
	}

	userId, role, err := getUser(username)
	if err != nil {
		http.Error(w, "User not found", 401)
		return
	}

	// 🔥 FIX: Normalize role
	role = strings.ToUpper(strings.TrimSpace(role))

	rows, err := func() (*sql.Rows, error) {
		if role == "ADMIN" {
			return DB.Query(`
				SELECT c.id,c.name,c.father_name,c.age,c.date_of_birth,
				       c.designation,c.email,u.name
				FROM crud_items c
				JOIN login_users u ON c.user_id=u.id`)
		}

		return DB.Query(`
			SELECT c.id,c.name,c.father_name,c.age,c.date_of_birth,
			       c.designation,c.email,u.name
			FROM crud_items c
			JOIN login_users u ON c.user_id=u.id
			WHERE c.user_id=@p1`, userId)
	}()

	if err != nil {
		http.Error(w, "Query failed", 500)
		return
	}
	defer rows.Close()

	var list []Crud
	for rows.Next() {
		var c Crud
		rows.Scan(
			&c.Id, &c.Name, &c.FatherName, &c.Age,
			&c.DateOfBirth, &c.Designation, &c.Email, &c.Username,
		)
		list = append(list, c)
	}

	json.NewEncoder(w).Encode(list)
}

/* =========================
   PUT (ADMIN ANY | USER OWN)
========================= */

func UpdateCrud(w http.ResponseWriter, r *http.Request) {
	id := chi.URLParam(r, "id")
	username := r.URL.Query().Get("username")

	// 🔥 DEBUG LOG
	log.Printf("DEBUG UpdateCrud: id=%s, username=%s", id, username)

	if username == "" {
		http.Error(w, "Username required", 400)
		return
	}

	if !canModify(id, username) {
		log.Printf("ERROR UpdateCrud: canModify returned false for id=%s, username=%s", id, username)
		http.Error(w, "Forbidden", 403)
		return
	}

	var c Crud
	if err := json.NewDecoder(r.Body).Decode(&c); err != nil {
		http.Error(w, "Invalid request body", 400)
		return
	}

	_, err := DB.Exec(`
		UPDATE crud_items
		SET name=@p1,father_name=@p2,age=@p3,
		    date_of_birth=@p4,designation=@p5,email=@p6
		WHERE id=@p7`,
		c.Name, c.FatherName, c.Age,
		c.DateOfBirth, c.Designation, c.Email, id)

	if err != nil {
		log.Printf("ERROR UpdateCrud: DB.Exec failed: %v", err)
		http.Error(w, "Update failed", 500)
		return
	}

	log.Printf("SUCCESS UpdateCrud: id=%s updated", id)
	json.NewEncoder(w).Encode(map[string]string{
		"message": "updated",
	})
}

/* =========================
   DELETE (ADMIN ANY | USER OWN)
========================= */

func DeleteCrud(w http.ResponseWriter, r *http.Request) {
	id := chi.URLParam(r, "id")
	username := r.URL.Query().Get("username")

	// 🔥 DEBUG LOG
	log.Printf("DEBUG DeleteCrud: id=%s, username=%s", id, username)

	if username == "" {
		http.Error(w, "Username required", 400)
		return
	}

	if !canModify(id, username) {
		log.Printf("ERROR DeleteCrud: canModify returned false for id=%s, username=%s", id, username)
		http.Error(w, "Forbidden", 403)
		return
	}

	_, err := DB.Exec(`DELETE FROM crud_items WHERE id=@p1`, id)
	if err != nil {
		log.Printf("ERROR DeleteCrud: DB.Exec failed: %v", err)
		http.Error(w, "Delete failed", 500)
		return
	}

	log.Printf("SUCCESS DeleteCrud: id=%s deleted", id)
	json.NewEncoder(w).Encode(map[string]string{
		"message": "deleted",
	})
}
