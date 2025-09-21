package com.civa.platform.iam.infrastructure.tokens.jwt;

import com.civa.platform.iam.application.internal.outboundedservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;


public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);
    String generateToken(Authentication authentication);
}
