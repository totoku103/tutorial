package com.example.generator.repository;

import com.example.generator.entity.ClientInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientInfoRepository extends JpaRepository<ClientInfoEntity, Long> {
}
