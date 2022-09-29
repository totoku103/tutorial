package me.totoku103.tutorial.gateway.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.totoku103.tutorial.gateway.model.TokenUser;

import java.util.HashSet;
import java.util.Set;


public class JwtUtils {

    private static final String CLAIM_SCOPE = "scope";

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