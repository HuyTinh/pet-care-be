package com.pet_care.identity_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pet_care.identity_service.client.FacebookClient;
import com.pet_care.identity_service.dto.request.*;
import com.pet_care.identity_service.dto.response.AuthenticationResponse;
import com.pet_care.identity_service.dto.response.IntrospectResponse;
import com.pet_care.identity_service.enums.Provide;
import com.pet_care.identity_service.exception.APIException;
import com.pet_care.identity_service.exception.ErrorCode;
import com.pet_care.identity_service.model.Account;
import com.pet_care.identity_service.model.FacebookUserInfo;
import com.pet_care.identity_service.model.InvalidatedToken;
import com.pet_care.identity_service.model.Role;
import com.pet_care.identity_service.repository.AccountRepository;
import com.pet_care.identity_service.repository.InvalidatedTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    @NotNull AccountRepository accountRepository;

    @NotNull InvalidatedTokenRepository invalidatedTokenRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @NotNull MessageService messageService;

    @NotNull ObjectMapper objectMapper;

    @NotNull FacebookClient facebookClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(@NotNull IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean validToken = true;

        try {
            verifyToken(token);
        } catch (APIException e) {
            validToken = false;
        }

        return IntrospectResponse
                .builder()
                .valid(validToken)
                .build();
    }

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request) {
        var account = accountRepository.findByEmail(request.getEmail()).orElseThrow(() -> new APIException(ErrorCode.EMAIL_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!authenticated) {
            throw new APIException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        var token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private String generateToken(@NotNull Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer("pet_care")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(
                        Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .claim("scope", buildScope(account))
                .claim("userId", account.getId())
                .build();


        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new APIException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private AuthenticationResponse authenticationResponse(@NotNull Account account) {
        var token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    public AuthenticationResponse refreshToken(@NotNull RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken());

        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expirationTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(jit)
                .expriryDate(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var email = signJWT.getJWTClaimsSet().getSubject();

        var account = accountRepository.findByEmail(email).orElseThrow(() -> new APIException(ErrorCode.EMAIL_NOT_EXISTED));

        var token = generateToken(account);

        return AuthenticationResponse.builder().token(token).isAuthenticated(true).build();
    }

    public void logout(@NotNull LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();

        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(jit)
                .expriryDate(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);


    }

    @NotNull
    private SignedJWT verifyToken(@NotNull String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new APIException(ErrorCode.UNAUTHENTICATED);
        }

        invalidatedTokenRepository.findById(signedJWT.getJWTClaimsSet().getJWTID()).ifPresent(invalidatedToken -> {
            throw new APIException(ErrorCode.UNAUTHENTICATED);
        });

        return signedJWT;
    }

    private String buildScope(@NotNull Account account) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty((account.getRoles()))) {
            account.getRoles().forEach(
                    role -> {
                        stringJoiner.add("ROLE_" + role.getName());
                        if (!CollectionUtils.isEmpty(role.getPermissions())) {
                            role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                        }
                    }
            );
        }
        return stringJoiner.toString();
    }

    public AuthenticationResponse authenticateWithGoogle(String accessToken) {

        try {
            // Build credential from the access token
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

            // Set up Oauth2 service
            Oauth2 oauth2 = new Oauth2.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
            ).setApplicationName("Pet Care")
                    .build();

            // Get user info
            Userinfo userInfo = oauth2.userinfo().get().execute();
            Account account = accountRepository.findByEmail(userInfo.getEmail()).orElse(null);

            if (account == null) {
                account = Account.builder()
                        .email(userInfo.getEmail())
                        .roles(Set.of(Role.builder().name("CUSTOMER").build()))
                        .provide(Provide.GOOGLE)
                        .build();

                Account saveAccount = accountRepository.save(account);

                CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                        .accountId(saveAccount.getId())
                        .email(userInfo.getEmail())
                        .firstName(userInfo.getFamilyName())
                        .lastName(userInfo.getGivenName())
                        .imageUrl(userInfo.getPicture())
                        .build();

                messageService.sendMessageQueue("customer-create-queue", objectMapper.writeValueAsString(customerCreateRequest));
            }

            return authenticationResponse(account);
        } catch (Exception e) {
            System.out.println(e);
            throw new APIException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public AuthenticationResponse authenticateWithFacebook(String accessToken) {
        try {
            FacebookUserInfo facebookUserInfo = facebookClient.getUserProfile("name, email,first_name,last_name,gender,picture{url}", accessToken);

            Account account = accountRepository.findByEmail(facebookUserInfo.getEmail()).orElse(null);

            if (account == null) {
                account = Account.builder()
                        .email(facebookUserInfo.getEmail())
                        .roles(Set.of(Role.builder().name("CUSTOMER").build()))
                        .provide(Provide.FACEBOOK)
                        .build();

                Account saveAccount = accountRepository.save(account);

                CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                        .accountId(saveAccount.getId())
                        .email(facebookUserInfo.getEmail())
                        .firstName(facebookUserInfo.getFirstName())
                        .lastName(facebookUserInfo.getLastName())
                        .build();

                messageService.sendMessageQueue("customer-create-queue", objectMapper.writeValueAsString(customerCreateRequest));
            }

            return authenticationResponse(account);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
