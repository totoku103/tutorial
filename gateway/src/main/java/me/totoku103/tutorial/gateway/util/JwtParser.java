package me.totoku103.tutorial.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import me.totoku103.tutorial.gateway.model.TokenUser;

import java.util.HashSet;
import java.util.Set;


@Slf4j
public class JwtParser {

    private static final String secret = "jwtKey";
    private static final String CLAIM_SCOPE = "scope";

    private static final JWTVerifier verifier;

    static {
        verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        log.info("create jwt verifier. {}", verifier);
    }

    public static boolean verify(String token) {
        try {
            verifier.verify(removeBearer(token));
            return true;
        } catch (Exception e) {
            log.error("exception: {}, token: {}", e, token);
            return false;
        }
    }

    public static String removeBearer(String token) {
        return token.substring("bearer ".length());
    }

    public static TokenUser decode(String token) {
        final DecodedJWT jwt = JWT.decode(removeBearer(token));

        final String id = jwt.getSubject();
        final Set<String> scope = jwt.getClaim(CLAIM_SCOPE).as(HashSet.class);

        return new TokenUser(id, scope);
    }

}