package me.totoku103.tutorial.authorization.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.totoku103.tutorial.authorization.service.CustomUserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomUserDetailService customUserDetailService;

    @GetMapping("/user/me")
    public ResponseEntity<UserDetails> getUserInfo() {
        UserDetails userDetails = customUserDetailService.loadUserByUsername("totoku103");
        return ResponseEntity.ok(userDetails);
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> callback(@RequestParam String code, @RequestParam String state) {
        log.info("code: {}, state: {}", code, state);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> addClientInfo(@RequestBody Map<String, String> obj) {
        return ResponseEntity.ok().build();
    }
}
