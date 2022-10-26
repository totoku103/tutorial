package me.totoku103.tutorial.authorization;

import com.nimbusds.jose.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthorizationOldApplicationTests {

    private final MockMvc mockMvc;

    AuthorizationOldApplicationTests(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "clientId", password = "secret")
    public void clientCredentialsTest_200() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/oauth/token")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("grant_type", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue())
                        .param("scope", "scopeA scopeB"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token_type").value("bearer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expires_in").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.scope").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.scope").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorities").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorities").isArray());
    }

    @Test
    @WithMockUser(username = "notFoundUser", password = "secret")
    public void clientCredentialsTest_401() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/oauth/token")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("grant_type", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue())
                        .param("scope", "scopeA scopeB"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "totoku103", password = "totoku103")
    public void authorizationCodeTest_0() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/oauth/authorize")
                        .param("client_id", "clientId")
                        .param("redirect_uri", "http://127.0.0.1:8080/callback")
                        .param("response_type", "code")
                        .param("scope", "scopeA scopeB")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/oauth/confirm_access"));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/oauth/authorize")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encode("totoku103:totoku103"))
                        .param("user_oauth_approval", "true")
                        .param("scope.scopeA", "true")
                        .param("scope.scopeB", "true")
                        .param("authorize", "Authorize")
                );
    }

    @Test
    @WithMockUser(username = "clientId", password = "secret")
    public void passwordTest_200() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/oauth/token")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("grant_type", "password")
                        .param("scope", "scopeA")
                        .param("username", "totoku103")
                        .param("password", "totoku103"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token_type").value("bearer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expires_in").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.scope").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.scope").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorities").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorities").isArray());
    }

    @Test
    @WithMockUser(username = "totoku103", password = "totoku103")
    public void implicit_200() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/oauth/authorize")
                        .param("client_id", "clientId")
                        .param("redirect_uri", "http://127.0.0.1:8080/callback")
                        .param("response_type", "token")
                        .param("scope", "scopeA"))
                .andExpect(status().is3xxRedirection());

    }
}
