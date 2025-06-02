package com.stc.rentalplatform.auth;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.ws.rs.core.HttpHeaders;

public class AuthHelper {

    public static String getAccessToken(String username, String password, String keycloakUrl) {
        WebClient client = WebClient.create();
        String response = client.post()
                .uri(keycloakUrl + "/realms/test-realm/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "rental-client")
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return JsonPath.read(response, "$.access_token");
    }
}
