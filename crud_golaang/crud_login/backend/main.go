package main

import (
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"
)

func main() {
	ConnectDB()

	r := chi.NewRouter()

	r.Use(cors.Handler(cors.Options{
		AllowedOrigins: []string{"http://localhost:4200"},
		AllowedMethods: []string{"GET", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders: []string{"Content-Type"},
	}))

	r.Get("/api/crud", GetCrud)
	r.Put("/api/crud/{id}", UpdateCrud)
	r.Delete("/api/crud/{id}", DeleteCrud)

	http.ListenAndServe(":9090", r)
}
