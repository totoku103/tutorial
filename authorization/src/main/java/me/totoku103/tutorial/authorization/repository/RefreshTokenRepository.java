package me.totoku103.tutorial.authorization.repository;

import me.totoku103.tutorial.authorization.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity findByTokenId(String tokenId);
    @Transactional
    int deleteByTokenId(String tokenId);

}
