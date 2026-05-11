package org.pulsar.bank.controller;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.request.CardCreateRequest;
import org.pulsar.bank.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardRestController {

    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Validated CardCreateRequest cardCreateRequest) {
        cardService.createCard(cardCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
