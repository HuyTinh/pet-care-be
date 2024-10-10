package com.pet_care.identity_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pet_care.identity_service.dto.response.APIResponse;
import com.pet_care.identity_service.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        String token = request.getHeader("Authorization").substring(7);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date expirationTime = claimsSet.getExpirationTime();

            // Kiểm tra xem token có hết hạn không
            if (expirationTime != null && expirationTime.before(new Date())) {
                errorCode = ErrorCode.TOKEN_HAS_EXPIRED;
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        APIResponse<?> apiResponse = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
