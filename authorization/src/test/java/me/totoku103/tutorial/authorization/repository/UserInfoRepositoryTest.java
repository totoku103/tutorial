package me.totoku103.tutorial.authorization.repository;

import lombok.RequiredArgsConstructor;
import me.totoku103.tutorial.authorization.entity.UserInfoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserInfoRepositoryTest {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    public void getTest() {
        UserInfoEntity totoku103 = userInfoRepository.findByUserId("totoku103");
        Assertions.assertNotNull(totoku103);
        Assertions.assertTrue(totoku103.getAuthorities().size() != 0);
    }
}