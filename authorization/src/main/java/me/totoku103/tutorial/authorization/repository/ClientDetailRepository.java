package me.totoku103.tutorial.authorization.repository;

import me.totoku103.tutorial.authorization.entity.ClientDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDetailRepository extends JpaRepository<ClientDetailEntity, String> {

    void deleteByClientId(String clientId);
}
