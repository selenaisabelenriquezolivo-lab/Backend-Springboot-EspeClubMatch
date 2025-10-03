package com.especlub.match.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostgresExampleController {
    @GetMapping("/psql-test")
    public String testConnection() {
        return "Conexión a PostgreSQL exitosa!";
    }
}

