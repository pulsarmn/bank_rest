package org.pulsar.bank.security.jwt;

import java.util.List;
import java.util.Map;

public interface JwtClaims {

    Object getClaim(String name);

    String getSubject();

    Map<String, Object> getClaims();

    List<Object> getClaimAsList(String name);

    List<String> getClaimAsStringList(String name);

    static DefaultJwtClaims.Builder builder() {
        return new DefaultJwtClaims.Builder();
    }

    interface Builder {

        Builder subject(String sub);

        Builder claim(String name, Object value);

        JwtClaims build();
    }
}
