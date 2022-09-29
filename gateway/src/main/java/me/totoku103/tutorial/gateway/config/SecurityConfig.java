package me.totoku103.tutorial.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity)  {
        httpSecurity
                .authorizeExchange()
                .pathMatchers("/resource").authenticated()
                .anyExchange().authenticated();
        return httpSecurity.build();
    }

//    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.debug(true);
            }
        };
    }
}
