package me.totoku103.tutorial.authorization.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.totoku103.tutorial.authorization.service.CustomUserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Deprecated
@RestController
@RequiredArgsConstructor
public class UserController {

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> callback(@RequestParam(required = false) String code,
                                         @RequestParam(required = false) String state) {
        log.info("code: {}, state: {}", code, state);
        return ResponseEntity.ok().build();
    }

}
