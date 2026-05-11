package org.pulsar.bank.repository;

import org.pulsar.bank.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByPanHash(String panHash);
}
