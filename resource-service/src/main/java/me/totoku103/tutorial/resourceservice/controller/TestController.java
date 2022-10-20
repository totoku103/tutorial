package me.totoku103.tutorial.resourceservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
public class TestController {

    @GetMapping("/test1")
    public ResponseEntity<LocalDateTime> requestTest1() {
        return ResponseEntity.ok(LocalDateTime.now());
    }

    @GetMapping("/test2")
    public ResponseEntity<LocalDateTime> requestTest2() {
        return ResponseEntity.ok(LocalDateTime.now());
    }
}
