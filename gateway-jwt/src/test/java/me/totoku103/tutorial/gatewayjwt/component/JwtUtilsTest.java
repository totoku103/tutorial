package me.totoku103.tutorial.gatewayjwt.component;

import me.totoku103.tutorial.gatewayjwt.model.TokenUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    JwtUtils jwtUtils;

    @Test
    public void test() {
        TokenUser tokenUser = new TokenUser("idid", "ROLE_READ");
        String generate = jwtUtils.generate(tokenUser);
        System.out.println(generate);
    }

}