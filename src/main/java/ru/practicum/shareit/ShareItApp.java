package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ShareItApp {
    public static void main(String[] args) {
        log.info("Start application");
        SpringApplication.run(ShareItApp.class, args);
    }
}