package org.pulsar.bank.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.pulsar.bank.dto.request.CardCreateRequest;
import org.pulsar.bank.entity.Card;
import org.pulsar.bank.entity.User;
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

    private static final int MAX_ATTEMPTS = 5;

    @Transactional
    public void createCard(CardCreateRequest cardCreateRequest) {
        Objects.requireNonNull(cardCreateRequest);

        Card card = userRepository.findByLogin(cardCreateRequest.login())
                .map(cardFactory::create)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            try {
                cardRepository.save(card);
                break;
            } catch (ConstraintViolationException ignored) {
            }
        }
    }
}
