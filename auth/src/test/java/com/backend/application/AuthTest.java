package com.backend.application;

import com.backend.application.domain.Role;
import com.backend.application.domain.User;
import com.backend.application.dto.JwtResponse;
import com.backend.application.dto.RefreshJwtRequest;
import com.backend.application.dto.requests.SignRequest;
import com.backend.application.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTest {

    @MockBean
    UserService userService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @InjectMocks
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @LocalServerPort
    int port;

    @SneakyThrows
    @Test
    @DisplayName("Аутентификация и получение токенов")
    public void AuthSignAndGetTokensTest() {

        User user = new User("username1", bCryptPasswordEncoder.encode("username1"), Role.Intern);
        Mockito.when(userService.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

        SignRequest signRequest = new SignRequest("username1", "username1");

        String urlAuth = "http://localhost:" + port + "/auth/sign";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SignRequest> requestAuth = new HttpEntity<>(signRequest, httpHeaders);

        ResponseEntity<JwtResponse> responseAuth = testRestTemplate.postForEntity(urlAuth, requestAuth, JwtResponse.class);

        System.out.println("*************************************");
        System.out.println("STATUS CODE: " + responseAuth.getStatusCode());
        System.out.println("TYPE: " + responseAuth.getBody().getType());
        System.out.println("ACCESS TOKEN: " + responseAuth.getBody().getAccessToken());
        System.out.println("REFRESH TOKEN: " + responseAuth.getBody().getRefreshToken());

        Assertions.assertEquals(200, responseAuth.getStatusCodeValue());
        Assertions.assertEquals("Bearer", responseAuth.getBody().getType());
    }

    @SneakyThrows
    @Test
    @DisplayName("Обновление access-токена")
    public void GetNewAccessTokenTest() {

        User user = new User("username1", bCryptPasswordEncoder.encode("username1"), Role.Intern);
        Mockito.when(userService.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

        SignRequest signRequest = new SignRequest("username1", "username1");

        String urlAuth = "http://localhost:" + port + "/auth/sign";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SignRequest> requestAuth = new HttpEntity<>(signRequest, httpHeaders);

        ResponseEntity<JwtResponse> responseAuth = testRestTemplate.postForEntity(urlAuth, requestAuth, JwtResponse.class);

        System.out.println("***************************************");
        System.out.println("CURRENT ACCESS TOKEN: " + responseAuth.getBody().getAccessToken());
        System.out.println("CURRENT REFRESH TOKEN: " + responseAuth.getBody().getRefreshToken());

        String urlAccess = "http://localhost:" + port + "/auth/access";

        String refreshToken = responseAuth.getBody().getRefreshToken();
        String accessToken = responseAuth.getBody().getAccessToken();

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken(refreshToken);

        HttpHeaders httpHeadersAccess = new HttpHeaders();
        httpHeadersAccess.setContentType(MediaType.APPLICATION_JSON);
        httpHeadersAccess.setBearerAuth(accessToken);
        Thread.sleep(1000);

        HttpEntity<RefreshJwtRequest> requestAccess = new HttpEntity<>(refreshJwtRequest, httpHeadersAccess);

        ResponseEntity<JwtResponse> responseAccess = testRestTemplate.postForEntity(urlAccess, requestAccess, JwtResponse.class);

        System.out.println("***************************************");
        System.out.println("STATUS CODE: " + responseAccess.getStatusCode());
        System.out.println("TYPE: " + responseAccess.getBody().getType());
        System.out.println("NEW ACCESS TOKEN: " + responseAccess.getBody().getAccessToken());
        System.out.println("NEW REFRESH TOKEN: " + responseAccess.getBody().getRefreshToken());

        Assertions.assertEquals(200, responseAccess.getStatusCodeValue());
        Assertions.assertEquals("Bearer", responseAccess.getBody().getType());
        Assertions.assertNotEquals(responseAuth.getBody().getAccessToken(), responseAccess.getBody().getAccessToken());
        Assertions.assertEquals(null, responseAccess.getBody().getRefreshToken());
    }

    @SneakyThrows
    @Test
    @DisplayName("Обновление всех токенов")
    public void RefreshAllTokensTest() {

        User user = new User("username1", bCryptPasswordEncoder.encode("username1"), Role.Intern);
        Mockito.when(userService.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

        SignRequest signRequest = new SignRequest("username1", "username1");

        String urlAuth = "http://localhost:" + port + "/auth/sign";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SignRequest> requestAuth = new HttpEntity<>(signRequest, httpHeaders);

        ResponseEntity<JwtResponse> responseAuth = testRestTemplate.postForEntity(urlAuth, requestAuth, JwtResponse.class);

        System.out.println("***************************************");
        System.out.println("CURRENT ACCESS TOKEN: " + responseAuth.getBody().getAccessToken());
        System.out.println("CURRENT REFRESH TOKEN: " + responseAuth.getBody().getRefreshToken());

        String urlRefresh = "http://localhost:" + port + "/auth/refresh";

        String refreshToken = responseAuth.getBody().getRefreshToken();
        String accessToken = responseAuth.getBody().getAccessToken();

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken(refreshToken);

        HttpHeaders httpHeadersAccess = new HttpHeaders();
        httpHeadersAccess.setContentType(MediaType.APPLICATION_JSON);
        httpHeadersAccess.set("Authorization", "Bearer " + accessToken);
        Thread.sleep(1000);

        HttpEntity<RefreshJwtRequest> requestRefresh = new HttpEntity<>(refreshJwtRequest, httpHeadersAccess);

        ResponseEntity<JwtResponse> responseRefresh = testRestTemplate.postForEntity(urlRefresh, requestRefresh, JwtResponse.class);

        System.out.println("***************************************");
        System.out.println("STATUS CODE: " + responseRefresh.getStatusCode());
        System.out.println("TYPE: " + responseRefresh.getBody().getType());
        System.out.println("NEW ACCESS TOKEN: " + responseRefresh.getBody().getAccessToken());
        System.out.println("NEW REFRESH TOKEN: " + responseRefresh.getBody().getRefreshToken());

        Assertions.assertEquals(200, responseRefresh.getStatusCodeValue());
        Assertions.assertEquals("Bearer", responseRefresh.getBody().getType());
        Assertions.assertNotEquals(responseAuth.getBody().getAccessToken(), responseRefresh.getBody().getAccessToken());
        Assertions.assertNotEquals(responseAuth.getBody().getRefreshToken(), responseRefresh.getBody().getRefreshToken());
    }

    @SneakyThrows
    @Test
    @DisplayName("Обновление access-токена по просроченному refresh-токену")
    public void GetNewAccessTokenWithExpiredRefreshTokenTest() {

        String urlAccess = "http://localhost:" + port + "/auth/access";

        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1ODkxNjM0N30" +
                ".mznvFayoqm9p8oQlOfF4l1562nb4lfbNzDApDRXfir9dRw5d9R1_kk96wgDIktqfeXVXxbHFYgvkuuS4TuVC2Q";
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImV4cCI6MTY3MDQ5NTUzNn0" +
                ".LxsLjL9QMVlSjTVBD9vLokUkqV3pRzQRB14mHldA8yhfq9k03OpwK04I-BKx2unF978pW4TiF3l2_NguqPNjPg";

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken(refreshToken);

        HttpHeaders httpHeadersAccess = new HttpHeaders();
        httpHeadersAccess.setContentType(MediaType.APPLICATION_JSON);
        httpHeadersAccess.setBearerAuth(accessToken);
        Thread.sleep(1000);

        HttpEntity<RefreshJwtRequest> requestAccess = new HttpEntity<>(refreshJwtRequest, httpHeadersAccess);

        ResponseEntity<JwtResponse> responseAccess = testRestTemplate.postForEntity(urlAccess, requestAccess, JwtResponse.class);

        System.out.println("*************************************");
        System.out.println("STATUS CODE: " + responseAccess.getStatusCode());
        System.out.println("TYPE: " + responseAccess.getBody().getType());
        System.out.println("NEW ACCESS TOKEN: " + responseAccess.getBody().getAccessToken());
        System.out.println("NEW REFRESH TOKEN: " + responseAccess.getBody().getRefreshToken());

        Assertions.assertEquals(200, responseAccess.getStatusCodeValue());
        Assertions.assertEquals("Bearer", responseAccess.getBody().getType());
        Assertions.assertEquals(null, responseAccess.getBody().getAccessToken());
    }

    @SneakyThrows
    @Test
    @DisplayName("Обновление access-токена по не корректному refresh-токену")
    public void GetNewAccessTokenWithIncorrectRefreshTokenTest() {

        String urlAccess = "http://localhost:" + port + "/auth/access";

        String refreshToken = "eyjhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1ODkxNjM0N30" +
                ".mznvFayoqm9p8oQlOfF4l1562nb4lfbNzDApDRXfir9dRw5d9R1_kk96wgDAAAAAAXVXxbHFYgvkuuS4TuVC2Q";
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1NjMyMDU3MSwicm9sZ" +
                "XMiOlsiQURNSU4iXSwiZmlyc3ROYW1lIjoiSmVycnkifQ" +
                ".8F3N1SuFlfgkumiU5rmDjoLnBBqjBwF4xDKanJ6FeYNfPuaatJqdY6FfpBx0YzSeDlOG6zjPTT-KMYGt_yYcvQ";

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken(refreshToken);

        HttpHeaders httpHeadersAccess = new HttpHeaders();
        httpHeadersAccess.setContentType(MediaType.APPLICATION_JSON);
        httpHeadersAccess.setBearerAuth(accessToken);
        Thread.sleep(1000);

        HttpEntity<RefreshJwtRequest> requestAccess = new HttpEntity<>(refreshJwtRequest, httpHeadersAccess);

        ResponseEntity<JwtResponse> responseAccess = testRestTemplate.postForEntity(urlAccess, requestAccess, JwtResponse.class);

        System.out.println("*************************************");
        System.out.println("STATUS CODE: " + responseAccess.getStatusCode());
        System.out.println("TYPE: " + responseAccess.getBody().getType());
        System.out.println("NEW ACCESS TOKEN: " + responseAccess.getBody().getAccessToken());
        System.out.println("NEW REFRESH TOKEN: " + responseAccess.getBody().getRefreshToken());

        Assertions.assertEquals(200, responseAccess.getStatusCodeValue());
        Assertions.assertEquals("Bearer", responseAccess.getBody().getType());
        Assertions.assertEquals(null, responseAccess.getBody().getAccessToken());
    }

    @Test
    @DisplayName("Обновление токенов по просроченным токенам")
    public void RefreshAllTokensWithExpiredTokensTest() {

        String urlRefresh = "http://localhost:" + port + "/auth/refresh";

        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1ODkxNjM0N30" +
                ".mznvFayoqm9p8oQlOfF4l1562nb4lfbNzDApDRXfir9dRw5d9R1_kk96wgDIktqfeXVXxbHFYgvkuuS4TuVC2Q";
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1NjMyMDU3MSwicm9sZ" +
                "XMiOlsiQURNSU4iXSwiZmlyc3ROYW1lIjoiSmVycnkifQ" +
                ".8F3N1SuFlfgkumiU5rmDjoLnBBqjBwF4xDKanJ6FeYNfPuaatJqdY6FfpBx0YzSeDlOG6zjPTT-KMYGt_yYcvQ";

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken(refreshToken);

        HttpHeaders httpHeadersRefresh = new HttpHeaders();
        httpHeadersRefresh.setContentType(MediaType.APPLICATION_JSON);
        httpHeadersRefresh.set("Authorization", "Bearer " + accessToken);

        HttpEntity<RefreshJwtRequest> requestRefresh = new HttpEntity<>(refreshJwtRequest, httpHeadersRefresh);

        ResponseEntity<JwtResponse> responseRefresh = testRestTemplate.postForEntity(urlRefresh, requestRefresh, JwtResponse.class);

        System.out.println("*************************************");
        System.out.println("STATUS CODE: " + responseRefresh.getStatusCode());

        Assertions.assertEquals(401, responseRefresh.getStatusCodeValue());
    }

    @Test
    @DisplayName("Обновление токенов по не корректным токенам")
    public void RefreshAllTokensWithIncorrectTokens_Test() {

        String urlRefresh = "http://localhost:" + port + "/auth/refresh";

        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1ODkxNjM0N30" +
                ".mznvFayoqm9p8oQlOfF4l1562nb4lfbNzDApDRXfir9dRw5d9R1_kk96wgDIktqfeXVXxbHFYgvkuuS4TuVC2Q";
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXJyeSIsImV4cCI6MTY1NjMyMDU3MSwicm9sZ" +
                "XMiOlsiQURNSU4iXSwiZmlyc3ROYW1lIjoiSmVycnkifQ" +
                ".8F3N1SuFlfgkumiU5rmDjoLnBBqjBwF4xDKanJ6FeYNfFFFFFJqdY6FfpBx0YzSeDlOG6zjPTT-KMYGt_yYcvQ";

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken(refreshToken);

        HttpHeaders httpHeadersRefresh = new HttpHeaders();
        httpHeadersRefresh.setContentType(MediaType.APPLICATION_JSON);
        httpHeadersRefresh.set("Authorization", "Bearer " + accessToken);

        HttpEntity<RefreshJwtRequest> requestRefresh = new HttpEntity<>(refreshJwtRequest, httpHeadersRefresh);

        ResponseEntity<JwtResponse> responseRefresh = testRestTemplate.postForEntity(urlRefresh, requestRefresh, JwtResponse.class);

        System.out.println("*************************************");
        System.out.println("STATUS CODE: " + responseRefresh.getStatusCode());

        Assertions.assertEquals(401, responseRefresh.getStatusCodeValue());
    }
}
