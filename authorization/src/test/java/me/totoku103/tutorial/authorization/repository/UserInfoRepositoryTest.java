package me.totoku103.tutorial.authorization.repository;

import me.totoku103.tutorial.authorization.entity.UserAuthorityEntity;
import me.totoku103.tutorial.authorization.entity.UserInfoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserInfoRepositoryTest {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    public void find_isNull() {
        final UserInfoEntity findResult = userInfoRepository.findByUserId("");
        Assertions.assertNull(findResult);
    }

    @Test
    public void save() {
        final String userId = "testId";

        final UserAuthorityEntity userAuthorityEntity1 = new UserAuthorityEntity();
        userAuthorityEntity1.setUserId(userId);
        userAuthorityEntity1.setAuthority("authority1");
        final UserAuthorityEntity userAuthorityEntity2 = new UserAuthorityEntity();
        userAuthorityEntity2.setUserId(userId);
        userAuthorityEntity2.setAuthority("authority2");

        final UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserId(userId);
        userInfoEntity.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"));
        userInfoEntity.setAuthorities(Set.of(userAuthorityEntity1, userAuthorityEntity2));

        final UserInfoEntity save = userInfoRepository.save(userInfoEntity);
        Assertions.assertNotNull(save);

        final UserInfoEntity findResult = userInfoRepository.findByUserId(userId);
        Assertions.assertNotNull(findResult);
        Assertions.assertEquals(userId, findResult.getUserId());
        Assertions.assertNotNull(findResult.getAuthorities());
        Assertions.assertTrue(findResult.getAuthorities().size() > 0);
    }

}