package me.totoku103.tutorial.gateway.filter;

import me.totoku103.tutorial.gateway.component.JwtUtils;
import me.totoku103.tutorial.gateway.model.TokenUser;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RefreshScope
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    private static final String ROLE_KEY = "scope";
    private static final String STRING_SPACE = " ";

    public JwtAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(ROLE_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();
            final ServerHttpResponse response = exchange.getResponse();

            if (!containsAuthorization(request)) {
                return onError(response, "missing authorization header", HttpStatus.BAD_REQUEST);
            }

            final String token = extractToken(request);
            TokenUser tokenUser;
            try {
                tokenUser = JwtUtils.decode(token);
            } catch (Exception e) {
                return onError(response, "invalid authorization header", HttpStatus.BAD_REQUEST);
            }

            if (!hasRole(tokenUser, config.scope)) {
                return onError(response, "invalid scope", HttpStatus.FORBIDDEN);
            }

            addAuthorizationHeaders(request, tokenUser);
            return chain.filter(exchange);
        };
    }

    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
    }

    private boolean hasRole(TokenUser tokenUser, Set<String> scope) {
        if (tokenUser == null || tokenUser.getScope() == null || tokenUser.getScope().size() == 0) return false;
        if (scope == null || scope.size() == 0) return false;

        final Set<String> userScopes = tokenUser.getScope();

        boolean result = false;
        for (String userScope : userScopes) {
            result = scope.contains(userScope);
            if (result)
                break;
        }
        return result;
    }

    private void addAuthorizationHeaders(ServerHttpRequest request, TokenUser tokenUser) {
        request.mutate()
                .header("X-Authorization-Id", tokenUser.getId())
                .header("X-Authorization-Scope", tokenUser.getScope().stream().collect(Collectors.joining(" ")))
                .build();
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        final DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
        private Set<String> scope;

        public void setScope(String scope) {
            this.scope = scope == null || scope.length() == 0
                    ? null
                    : Arrays.stream(scope.split(STRING_SPACE)).collect(Collectors.toSet());
        }
    }
}
