package org.example.ahd;

import org.example.ahd.utils.Encryption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println(Encryption.SHA256OneWayHash("1234"));
        SpringApplication.run(Main.class, args);
    }
}
