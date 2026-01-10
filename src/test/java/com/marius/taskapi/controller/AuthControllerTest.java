package com.marius.taskapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marius.taskapi.dto.AuthResponse;
import com.marius.taskapi.dto.LoginRequest;
import com.marius.taskapi.dto.RegisterRequest;
import com.marius.taskapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    
    @Mock
    private AuthService authService;
    
    @InjectMocks
    private AuthController authController;
    
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    
    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        
        loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("password123");
        
        authResponse = new AuthResponse();
        authResponse.setToken("jwt-token");
        authResponse.setUserId(1L);
        authResponse.setUsername("testuser");
        authResponse.setEmail("test@example.com");
    }
    
    @Test
    void testRegister_Success() {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);
        
        // Act
        ResponseEntity<AuthResponse> response = authController.register(registerRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
        assertEquals("testuser", response.getBody().getUsername());
    }
    
    @Test
    void testLogin_Success() {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);
        
        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
        assertEquals("testuser", response.getBody().getUsername());
    }
}
