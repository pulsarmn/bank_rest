package org.pulsar.bank.entity;

import java.util.UUID;

public class User {

    private UUID id;
    private String login;
    private String hashPassword;
    private Role role;
    private String firstName;
    private String lastName;
}
