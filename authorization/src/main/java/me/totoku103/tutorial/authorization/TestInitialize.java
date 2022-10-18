package me.totoku103.tutorial.authorization;

import lombok.RequiredArgsConstructor;
import me.totoku103.tutorial.authorization.entity.UserAuthorityEntity;
import me.totoku103.tutorial.authorization.entity.UserInfoEntity;
import me.totoku103.tutorial.authorization.repository.UserInfoRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.jpa.hibernate.ddl-auto", havingValue = "create")
public class TestInitialize implements ApplicationListener<ApplicationReadyEvent> {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUserInfo() {
        final UserInfoEntity webAdminUser = new UserInfoEntity();
        webAdminUser.setUserId("totoku103");
        webAdminUser.setPassword(passwordEncoder.encode("totoku103"));

        final Set<UserAuthorityEntity> authos = new HashSet<>();
        String[] authorities = {"restFul", "SOCKET"};
        for (String auth : authorities) {
            final UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
            userAuthorityEntity.setUserId(webAdminUser.getUserId());
            userAuthorityEntity.setAuthority(auth);
            authos.add(userAuthorityEntity);
        }

        webAdminUser.setAuthorities(authos);
        userInfoRepository.save(webAdminUser);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        saveUserInfo();
    }
}
