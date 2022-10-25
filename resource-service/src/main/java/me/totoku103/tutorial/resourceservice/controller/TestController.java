package me.totoku103.tutorial.resourceservice.controller;

import me.totoku103.tutorial.resourceservice.component.CustomThreadLocal;
import me.totoku103.tutorial.resourceservice.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }


    @GetMapping("/test1")
    public ResponseEntity<Integer> requestTest1() {
        CustomThreadLocal.increment(1);
        testService.increment();
        return ResponseEntity.ok(CustomThreadLocal.flush());
    }

    @GetMapping("/test2")
    public ResponseEntity<Integer> requestTest2() {
        CustomThreadLocal.increment(1);
        testService.increment();
        return ResponseEntity.ok(CustomThreadLocal.flush());
    }
}
