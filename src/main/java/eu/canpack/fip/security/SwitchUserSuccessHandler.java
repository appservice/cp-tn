package eu.canpack.fip.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import eu.canpack.fip.security.jwt.JWTConfigurer;
import eu.canpack.fip.security.jwt.TokenProvider;
import eu.canpack.fip.web.rest.UserJWTController;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CP S.A.
 * Created by lukasz.mochel on 26.10.2017.
 */
@Service
public class SwitchUserSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(SwitchUserSuccessHandler.class);

    private final TokenProvider tokenProvider;

    public SwitchUserSuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt = tokenProvider.createToken(authentication, true);
        response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);


        log.debug("swiched to authentication as {}", authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObjectResponse =   objectMapper.writeValueAsString(new SwitchUserSuccessHandler.JWTToken(jwt));
        response.getWriter().append(jsonObjectResponse);
        response.setStatus(200);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
