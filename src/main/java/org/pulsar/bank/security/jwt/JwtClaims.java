package org.pulsar.bank.security.jwt;

import java.util.Map;

public interface JwtClaims {

    Object getClaim(String name);

    String getSubject();

    Map<String, Object> getClaims();

    static DefaultJwtClaims.Builder builder() {
        return new DefaultJwtClaims.Builder();
    }

    interface Builder {

        Builder subject(String sub);

        Builder claim(String name, Object value);

        JwtClaims build();
    }
}
