package main

import (
	"database/sql"
	"fmt"
	"log"

	_ "github.com/denisenkom/go-mssqldb"
)

var DB *sql.DB

func ConnectDB() {
	conn := "sqlserver://sa:benchmatrix123@localhost:1433?database=crud"
	var err error
	DB, err = sql.Open("sqlserver", conn)
	if err != nil {
		log.Fatal(err)
	}

	if err = DB.Ping(); err != nil {
		log.Fatal(err)
	}

	fmt.Println("✅ SQL Server connected")
}
