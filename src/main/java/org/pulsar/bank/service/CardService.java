package org.pulsar.bank.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.request.CardCreateRequest;
import org.pulsar.bank.entity.Card;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.exception.CardCreationException;
import org.pulsar.bank.exception.UserNotFoundException;
import org.pulsar.bank.repository.CardRepository;
import org.pulsar.bank.repository.UserRepository;
import org.pulsar.bank.util.CardFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CardService {

    private final CardFactory cardFactory;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    private static final int MAX_REGENERATION_ATTEMPS = 5;

    @Transactional
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
}
