package com.coldrain.jwt.filter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtProperties {
    public static final long EXPIRATION_TIME = (60000 * 10);
    public static final String SECRET = "SECRET";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
