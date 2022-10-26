package me.totoku103.tutorial.authorization;

import lombok.RequiredArgsConstructor;
import me.totoku103.tutorial.authorization.entity.ClientDetailEntity;
import me.totoku103.tutorial.authorization.entity.UserAuthorityEntity;
import me.totoku103.tutorial.authorization.entity.UserInfoEntity;
import me.totoku103.tutorial.authorization.repository.ClientDetailRepository;
import me.totoku103.tutorial.authorization.repository.UserInfoRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.jpa.hibernate.ddl-auto", havingValue = "create")
public class TestInitialize implements ApplicationListener<ApplicationReadyEvent> {

    private final ClientDetailRepository clientDetailRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUserInfo() {
        final UserInfoEntity webAdminUser = new UserInfoEntity();
        webAdminUser.setUserId("totoku103");
        webAdminUser.setPassword(passwordEncoder.encode("totoku103"));

        final Set<UserAuthorityEntity> authos = new HashSet<>();
        String[][] authorities = {{"마스터", "M"}, {"그룹관리자", "G"}, {"일반관리자", "S"}};
        for (String[] auth : authorities) {
            final UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
            userAuthorityEntity.setUserId(webAdminUser.getUserId());
            userAuthorityEntity.setAuthority(auth[0]);
            userAuthorityEntity.setLevel(auth[1]);
            authos.add(userAuthorityEntity);
        }

        webAdminUser.setAuthorities(authos);
        userInfoRepository.save(webAdminUser);
    }

    public void saveClientDetailInfo() {
        final String GRANT_TYPES = List.of(AuthorizationGrantType.CLIENT_CREDENTIALS,
                        AuthorizationGrantType.AUTHORIZATION_CODE,
                        AuthorizationGrantType.PASSWORD,
                        AuthorizationGrantType.REFRESH_TOKEN)
                .stream()
                .map(v -> v.getValue())
                .collect(Collectors.joining(","));

        final ClientDetailEntity clientDetailEntity = new ClientDetailEntity();
        clientDetailEntity.setClientId("clientId");
        clientDetailEntity.setScope("scopeA,scopeB");
        clientDetailEntity.setClientSecret(passwordEncoder.encode("secret"));
        clientDetailEntity.setAdditionalInformation("{ \"key1\": \"value\" }");
        clientDetailEntity.setAuthorities("authority1,authority2");
        clientDetailEntity.setRefreshTokenValidity(5 * 60 * 60);
        clientDetailEntity.setAccessTokenValidity(1 * 60 * 60);
        clientDetailEntity.setWebServerRedirectUri("http://127.0.0.1:8080/callback");
        clientDetailEntity.setAuthorizedGrantTypes(GRANT_TYPES);

        clientDetailRepository.save(clientDetailEntity);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        saveUserInfo();
        saveClientDetailInfo();
    }
}
