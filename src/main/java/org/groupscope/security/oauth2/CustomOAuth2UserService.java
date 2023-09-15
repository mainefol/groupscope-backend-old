package org.groupscope.security.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.GroupScopeDAOImpl;
import org.groupscope.entity.Provider;
import org.groupscope.security.entity.CustomUser;
import org.groupscope.security.auth.CustomUserService;
import org.groupscope.security.dto.OAuth2Request;
import org.groupscope.security.dto.RegistrationRequest;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {

    private final CustomUserService customUserService;

    private final GoogleIdTokenVerifier idTokenVerifier;

    @Autowired
    public CustomOAuth2UserService(@Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
                                   CustomUserService customUserService) {
        this.customUserService = customUserService;

        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        this.idTokenVerifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Transactional
    public CustomUser loginOAuthGoogle(OAuth2Request request) {
        if(request.getIdToken() != null) {
            RegistrationRequest registrationRequest = verifyIDToken(request.getIdToken());
            if (registrationRequest == null) {
                throw new IllegalArgumentException("Token not verified");
            }
            CustomUser user = new CustomUser();
            user.setLogin(registrationRequest.getLogin());
            registrationRequest.setInviteCode(request.getInviteCode());
            registrationRequest.setGroupName(request.getGroupName());

            CustomUser foundUser = customUserService.findByLogin(user.getLogin());

            if(foundUser == null)
                foundUser = customUserService.saveUser(user, registrationRequest, Provider.GOOGLE);

            Hibernate.initialize(foundUser.getLearner().getGrades());

            if(foundUser.getLearner().getLearningGroup() != null)
                GroupScopeDAOImpl.removeDuplicates(foundUser.getLearner().getLearningGroup().getSubjects());

            return foundUser;
        } else
            throw new NullPointerException("IdToken is null");
    }

    private RegistrationRequest verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = idTokenVerifier.verify(idToken);
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();

            RegistrationRequest request = new RegistrationRequest();
            request.setLearnerName((String) payload.get("given_name"));
            request.setLearnerLastname((String) payload.get("family_name"));
            request.setLogin(payload.getEmail());
            return request;
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
