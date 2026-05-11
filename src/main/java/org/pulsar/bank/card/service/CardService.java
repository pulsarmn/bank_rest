package org.pulsar.bank.card.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.card.dto.CardActivateRequest;
import org.pulsar.bank.card.dto.CardBlockRequest;
import org.pulsar.bank.card.dto.CardCreateRequest;
import org.pulsar.bank.card.domain.Card;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.card.exception.CardCreationException;
import org.pulsar.bank.auth.exception.UserNotFoundException;
import org.pulsar.bank.card.exception.CardNotFoundException;
import org.pulsar.bank.card.repository.CardRepository;
import org.pulsar.bank.auth.repository.UserRepository;
import org.pulsar.bank.card.CardFactory;
import org.pulsar.bank.crypto.service.HmacService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final CardFactory cardFactory;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final HmacService hmacService;

    private static final int MAX_REGENERATION_ATTEMPS = 5;

    public void createCard(CardCreateRequest cardCreateRequest) {
        Objects.requireNonNull(cardCreateRequest);

        Card card = userRepository.findByLogin(cardCreateRequest.login())
                .map(this::generateUniqueCard)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        cardRepository.save(card);
    }

    private Card generateUniqueCard(User user) {
        for (int attempt = 1; attempt <= MAX_REGENERATION_ATTEMPS; attempt++) {
            Card card = cardFactory.create(user);

            if (!cardRepository.existsByPanHash(card.getPanHash())) {
                return card;
            }
        }
        throw new CardCreationException("Failed to create a unique card");
    }

    public void block(CardBlockRequest cardBlockRequest) {
        String cardPan = cardBlockRequest.cardPan();
        changeCardStatus(cardPan, Card.Status.BLOCKED);
    }

    public void activate(CardActivateRequest cardActivateRequest) {
        String cardPan = cardActivateRequest.cardPan();
        changeCardStatus(cardPan, Card.Status.ACTIVE);
    }

    private void changeCardStatus(String cardPan, Card.Status status) {
        String cardPanHash = hmacService.hash(cardPan);

        cardRepository.findByPanHash(cardPanHash)
                .map(card -> {
                    card.setStatus(status);
                    return card;
                })
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }
}
