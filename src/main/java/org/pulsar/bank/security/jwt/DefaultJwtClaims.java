package org.pulsar.bank.security.jwt;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


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

    @Override
    public List<Object> getClaimAsList(final String name) {
        Object value = getClaim(name);

        if (!(value instanceof List<?> raw)) {
            return List.of();
        }

        return raw.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<String> getClaimAsStringList(final String name) {
        List<Object> list = getClaimAsList(name);

        return list.stream()
                .filter(Objects::nonNull)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
    }

    public static class Builder implements JwtClaims.Builder {

        private final Map<String, Object> claims = new LinkedHashMap<>();

        @Override
        public Builder subject(final String sub) {
            claims.put("sub", sub);
            return this;
        }

        @Override
        public Builder claim(final String name, final Object value) {
            claims.put(name, value);
            return this;
        }

        @Override
        public JwtClaims build() {
            return new DefaultJwtClaims(claims);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
