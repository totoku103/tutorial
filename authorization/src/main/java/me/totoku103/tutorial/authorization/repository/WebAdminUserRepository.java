package me.totoku103.tutorial.authorization.repository;

import me.totoku103.tutorial.authorization.entity.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebAdminUserRepository extends JpaRepository<WebAdminUser, Integer> {
    WebAdminUser findByUserLogin(String userLogin);
}
