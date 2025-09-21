package com.backend.jvconstructions.controller;

import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserRepresentation> getUsers(@PathVariable String userId) {
        UserRepresentation user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        List<UserRepresentation> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestParam String userName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role
    ) {
        userService.createUser(userName, email, password, role);
        return ResponseEntity.ok("User created successfully!");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestParam String email) {
        userService.updateUser(userId, email);
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully!");
    }
}
