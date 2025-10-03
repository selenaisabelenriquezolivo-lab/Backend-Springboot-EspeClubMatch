package com.especlub.match;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EspeClubMatchApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // evita errores si no hay .env
                .load();

        // add DB config from .env
        System.setProperty("DB_URL", dotenv.get("DB_URL", ""));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME", ""));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD", ""));
        System.setProperty("SECRET_KEY_JWT", dotenv.get("SECRET_KEY_JWT", ""));
        System.setProperty("EXPIRATION_TIME_JWT", dotenv.get("EXPIRATION_TIME_JWT", ""));

        SpringApplication.run(EspeClubMatchApplication.class, args);
    }
}
