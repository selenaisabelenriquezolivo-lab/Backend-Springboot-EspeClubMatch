package com.especlub.match.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("api/v1/example")
public class ExampleController {
    @GetMapping("/test")
    public String ejemplo() {
        log.info("Ejecutando el endpoint /api/v1/example/test");
        return "¡Conexión a PostgreSQL lista y funcionando!";
    }
}
