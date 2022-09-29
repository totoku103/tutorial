package me.totoku103.tutorial.resourceservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<LocalDateTime> requestTest() {
        return ResponseEntity.ok(LocalDateTime.now());
    }
}
