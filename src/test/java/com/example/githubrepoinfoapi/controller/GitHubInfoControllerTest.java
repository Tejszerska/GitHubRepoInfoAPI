package com.example.githubrepoinfoapi.controller;

import com.example.githubrepoinfoapi.model.RepositoryInfo;
import com.example.githubrepoinfoapi.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GitHubInfoControllerTest {

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private GitHubInfoController gitHubInfoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRepositories_Success() {
        // Mock service response
        List<RepositoryInfo> mockRepositories = Collections.singletonList(new RepositoryInfo("repo1", "user1", Collections.emptyList()));
        when(gitHubService.getRepositories("user1")).thenReturn(mockRepositories);

        // Call the controller method
        List<RepositoryInfo> response = gitHubInfoController.getRepositories("user1", "application/json");

        // Verify the results
        assertEquals(mockRepositories, response);
    }

    @Test
    public void testGetRepositories_InvalidAcceptHeader() {
        // Call the controller method with invalid Accept header
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gitHubInfoController.getRepositories("user1", "text/plain");
        });

        // Verify the results
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Accept header must be 'application/json'", exception.getReason());
    }
}
