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
import com.pet_care.identity_service.enums.Gender;
import com.pet_care.identity_service.enums.Provide;
import com.pet_care.identity_service.exception.APIException;
import com.pet_care.identity_service.exception.ErrorCode;
import com.pet_care.identity_service.entity.Account;
import com.pet_care.identity_service.entity.FacebookUserInfo;
import com.pet_care.identity_service.entity.InvalidatedToken;
import com.pet_care.identity_service.entity.Role;
import com.pet_care.identity_service.repository.AccountRepository;
import com.pet_care.identity_service.repository.InvalidatedTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     AccountRepository accountRepository;

     InvalidatedTokenRepository invalidatedTokenRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

     MessageService messageService;

     ObjectMapper objectMapper;

     FacebookClient facebookClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    /**
     * @param request
     * @return
     * @throws JOSEException
     * @throws ParseException
     */
    @Transactional(readOnly = true)
    public IntrospectResponse introspect( IntrospectRequest request) throws JOSEException, ParseException {
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

    /**
     * @param request
     * @return
     */
    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate( AuthenticationRequest request) {
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

    /**
     * @param account
     * @return
     */
    private String generateToken(Account account) {
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
                .claim("user_id", account.getId())
                .build();


        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot createPermission token", e);
            throw new APIException(ErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * @param account
     * @return
     */
    private AuthenticationResponse authenticationResponse(Account account) {
        var token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    /**
     * @param request
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    @Transactional
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
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

    /**
     * @param request
     * @throws ParseException
     * @throws JOSEException
     */
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
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

    /**
     * @param token
     * @return
     * @throws JOSEException
     * @throws ParseException
     */
    
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
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

    /**
     * @param account
     * @return
     */
    private String buildScope(Account account) {
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

    /**
     * @param accessToken
     * @return
     */
    @Transactional
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
            throw new APIException(ErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * @param accessToken
     * @return
     */
    @Transactional
    public AuthenticationResponse authenticateWithFacebook(String accessToken) {
        try {
            FacebookUserInfo facebookUserInfo = facebookClient.getUserProfile("name, email, first_name, last_name, gender, picture{url}", accessToken);

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
                        .gender(Gender.valueOf(facebookUserInfo.getGender().toUpperCase()))
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
