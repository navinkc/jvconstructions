package com.backend.jvconstructions.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final Keycloak keycloak;
    private final String realm;

    public UserService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public UserRepresentation getUser(String userId) {
        return keycloak.realm(realm).users().get(userId).toRepresentation();
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm(realm).users().list();
    }

    public void createUser(String userName, String email, String password, String role) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userName);
        userRepresentation.setEmail(email);
        userRepresentation.setEnabled(true);

        Response response = keycloak.realm(realm).users().create(userRepresentation);
        if (response.getStatus() != 201) throw new RuntimeException("Failed to create user: " + response.getStatusInfo());

        String userId = CreatedResponseUtil.getCreatedId(response);

        // Set Password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        keycloak.realm(realm).users().get(userId).resetPassword(credentialRepresentation);

        // Assign Role
        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(role).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    public void updateUser(String userId, String email) {
        UserRepresentation userRepresentation = keycloak.realm(realm).users().get(userId).toRepresentation();
        userRepresentation.setEmail(email);
        keycloak.realm(realm).users().get(userId).update(userRepresentation);
    }

    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }
}
