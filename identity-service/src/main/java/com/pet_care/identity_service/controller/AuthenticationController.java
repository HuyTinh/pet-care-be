package com.pet_care.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.pet_care.identity_service.dto.request.AuthenticationRequest;
import com.pet_care.identity_service.dto.request.IntrospectRequest;
import com.pet_care.identity_service.dto.request.LogoutRequest;
import com.pet_care.identity_service.dto.request.RefreshRequest;
import com.pet_care.identity_service.dto.response.APIResponse;
import com.pet_care.identity_service.dto.response.AuthenticationResponse;
import com.pet_care.identity_service.dto.response.IntrospectResponse;
import com.pet_care.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
     AuthenticationService authenticationService;

    /**
     * @param request
     * @return
     */
    @PostMapping("token")
    APIResponse<AuthenticationResponse> authenticate( @RequestBody AuthenticationRequest request) {
        return APIResponse.<AuthenticationResponse>builder()
                .data(authenticationService.authenticate(request))
                .build();
    }

    /**
     * @param request
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    @PostMapping("refresh")
    APIResponse<AuthenticationResponse> refresh( @RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return APIResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refreshToken(request))
                .build();
    }

    /**
     * @param request
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    @PostMapping("introspect")
    APIResponse<IntrospectResponse> authenticate( @RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return APIResponse.<IntrospectResponse>builder()
                .data(authenticationService.introspect(request))
                .build();
    }

    /**
     * @param request
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    @PostMapping("/logout")
    APIResponse<Void> logout( @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder().build();
    }

    /**
     * @param accessToken
     * @return
     * @throws Exception
     */
    @PostMapping("/google")
    public APIResponse<AuthenticationResponse> authenticateWithGoogle(@RequestBody String accessToken) throws Exception {
        return APIResponse.<AuthenticationResponse>builder().data(authenticationService.authenticateWithGoogle(accessToken)).build();
    }

    /**
     * @param accessToken
     * @return
     * @throws Exception
     */
    @PostMapping("/facebook")
    public APIResponse<AuthenticationResponse> authenticateWithFacebook(@RequestBody String accessToken) throws Exception {
        return APIResponse.<AuthenticationResponse>builder().data(authenticationService.authenticateWithFacebook(accessToken)).build();
    }
}
