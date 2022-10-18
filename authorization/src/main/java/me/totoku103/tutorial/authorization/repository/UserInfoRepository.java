package me.totoku103.tutorial.authorization.repository;

import me.totoku103.tutorial.authorization.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
    UserInfoEntity findByUserId(String userId);
}
