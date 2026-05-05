package org.pulsar.bank.security;

import java.util.LinkedHashMap;
import java.util.Map;


public class DefaultJwtClaims implements JwtClaims {

    private final Map<String, Object> claims = new LinkedHashMap<>();

    private DefaultJwtClaims(final Map<String, Object> claims) {
        this.claims.putAll(claims);
    }

    @Override
    public Object getClaim(String name) {
        return claims.get(name);
    }

    @Override
    public String getSubject() {
        Object sub = getClaim("sub");
        if (sub instanceof String) {
            return (String) sub;
        }
        return null;
    }

    @Override
    public Map<String, Object> getClaims() {
        return new LinkedHashMap<>(claims);
    }

    public static class Builder {

        private final Map<String, Object> claims = new LinkedHashMap<>();

        public Builder subject(final String sub) {
            claims.put("sub", sub);
            return this;
        }

        public Builder claim(final String name, final Object value) {
            claims.put(name, value);
            return this;
        }

        public JwtClaims build() {
            return new DefaultJwtClaims(claims);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
