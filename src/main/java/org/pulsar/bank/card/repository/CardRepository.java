package org.pulsar.bank.card.repository;

import org.pulsar.bank.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByPanHash(String panHash);

    Optional<Card> findByPanHash(String panHash);
}
