package me.totoku103.tutorial.authorization.repository;

import me.totoku103.tutorial.authorization.entity.AccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, Long> {

    AccessTokenEntity findByTokenId(String tokenId);

    @Transactional
    int deleteByTokenId(String tokenId);

    @Transactional
    int deleteByRefreshToken(String refreshToken);

    AccessTokenEntity findByAuthenticationId(String authenticationId);

    List<AccessTokenEntity> findByUserNameAndClientId(String userName, String clientId);

    List<AccessTokenEntity> findByClientId(String clientId);
}
