package com.pet_care.identity_service.client;

import com.pet_care.identity_service.model.FacebookUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebookClient", url = "https://graph.facebook.com/v20.0")
public interface FacebookClient {

    @GetMapping("/me")
    FacebookUserInfo getUserProfile(@RequestParam("fields") String fields,
                                    @RequestParam("access_token") String accessToken);
}
