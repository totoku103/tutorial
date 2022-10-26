package me.totoku103.tutorial.authorization.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.totoku103.tutorial.authorization.entity.ClientDetailEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientDetailRepositoryTest {

    @Autowired
    private ClientDetailRepository clientDetailRepository;
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final String GRANT_TYPES = List.of(AuthorizationGrantType.CLIENT_CREDENTIALS,
                    AuthorizationGrantType.AUTHORIZATION_CODE,
                    AuthorizationGrantType.PASSWORD,
                    AuthorizationGrantType.REFRESH_TOKEN,
                    AuthorizationGrantType.IMPLICIT)
            .stream()
            .map(v -> v.getValue())
            .collect(Collectors.joining(","));

    @Test
    public void save() throws JsonProcessingException {
        final ClientDetailEntity clientDetailEntity = new ClientDetailEntity();
        clientDetailEntity.setClientId("clientId-test");
        clientDetailEntity.setScope("scopeA,scopeB");
        clientDetailEntity.setClientSecret(passwordEncoder.encode("secret-password"));
        clientDetailEntity.setAdditionalInformation("{ \"key1\": \"value\" }");
        clientDetailEntity.setAuthorities("authority1,authority2");
        clientDetailEntity.setRefreshTokenValidity(5 * 60 * 60);
        clientDetailEntity.setAccessTokenValidity(1 * 60 * 60);
        clientDetailEntity.setWebServerRedirectUri("http://127.0.0.1:8080/callback");
        clientDetailEntity.setAuthorizedGrantTypes(this.GRANT_TYPES);

        final ClientDetailEntity save = clientDetailRepository.save(clientDetailEntity);
        Assertions.assertNotNull(save);
    }

    @Test
    public void get() {
        final String findValue = "clientId";
        final ClientDetailEntity clientId = clientDetailRepository
                .findById(findValue)
                .orElseThrow(() -> new RuntimeException());

        Assertions.assertNotNull(clientId);
        Assertions.assertEquals(findValue, clientId.getClientId());
    }

    @Test
    public void delete() {
        final String clientId = "clientId-test";
        final ClientDetailEntity clientDetailEntity = new ClientDetailEntity();
        clientDetailEntity.setClientId(clientId);
        clientDetailEntity.setScope("scopeA scopeB");
        clientDetailEntity.setClientSecret(passwordEncoder.encode("secret-password"));
        clientDetailEntity.setAdditionalInformation("{ \"key1\": \"value\" }");
        clientDetailEntity.setAuthorities("authority1 authority2");
        clientDetailEntity.setRefreshTokenValidity(5 * 60 * 60);
        clientDetailEntity.setAccessTokenValidity(1 * 60 * 60);
        clientDetailEntity.setWebServerRedirectUri("http://127.0.0.1:8080/callback");
        clientDetailEntity.setAuthorizedGrantTypes(this.GRANT_TYPES);

        final ClientDetailEntity save = clientDetailRepository.save(clientDetailEntity);
        Assertions.assertNotNull(save);

        final ClientDetailEntity saveResult = clientDetailRepository.findById(clientId).get();
        Assertions.assertNotNull(saveResult);

        clientDetailRepository.deleteByClientId(clientId);
        final Optional<ClientDetailEntity> deleteResult = clientDetailRepository.findById(clientId);
        Assertions.assertTrue(deleteResult.isEmpty());
    }

}