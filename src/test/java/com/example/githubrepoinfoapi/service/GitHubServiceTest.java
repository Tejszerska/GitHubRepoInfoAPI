package com.example.githubrepoinfoapi.service;

import com.example.githubrepoinfoapi.model.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GitHubServiceTest {

    private GitHubService gitHubService;
    private RestTemplate mockRestTemplate;

    @BeforeEach
    public void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        gitHubService = new GitHubService(mockRestTemplate);
    }

    @Test
    public void testGetRepositories_Success() {
        // Mock user response
        Map<String, Object> mockUser = new HashMap<>();
        ResponseEntity<Map> userResponse = new ResponseEntity<>(mockUser, HttpStatus.OK);
        when(mockRestTemplate.getForEntity(anyString(), eq(Map.class))).thenReturn(userResponse);

        // Mock repositories response
        List<Map<String, Object>> mockRepos = new ArrayList<>();
        Map<String, Object> mockRepo = new HashMap<>();
        mockRepo.put("name", "repo1");
        mockRepo.put("owner", Map.of("login", "user1"));
        mockRepo.put("fork", false);
        mockRepo.put("branches_url", "https://api.github.com/repos/user1/repo1/branches{/branch}");
        mockRepos.add(mockRepo);
        ResponseEntity<List> reposResponse = new ResponseEntity<>(mockRepos, HttpStatus.OK);
        when(mockRestTemplate.getForEntity(contains("/repos"), eq(List.class))).thenReturn(reposResponse);

        // Mock branches response
        List<Map<String, Object>> mockBranches = new ArrayList<>();
        Map<String, Object> mockBranch = new HashMap<>();
        mockBranch.put("name", "main");
        mockBranch.put("commit", Map.of("sha", "123456"));
        mockBranches.add(mockBranch);
        ResponseEntity<List> branchesResponse = new ResponseEntity<>(mockBranches, HttpStatus.OK);
        when(mockRestTemplate.getForEntity(contains("/branches"), eq(List.class))).thenReturn(branchesResponse);

        // Call the method
        List<RepositoryInfo> repositories = gitHubService.getRepositories("user1");

        // Verify the results
        assertNotNull(repositories);
        assertEquals(1, repositories.size());
        assertEquals("repo1", repositories.get(0).getRepositoryName());
        assertEquals("user1", repositories.get(0).getOwnerLogin());
        assertEquals(1, repositories.get(0).getBranches().size());
        assertEquals("main", repositories.get(0).getBranches().get(0).getBranchName());
        assertEquals("123456", repositories.get(0).getBranches().get(0).getLastCommitSha());
    }

    @Test
    public void testGetRepositories_UserNotFound() {
        // Mock user not found response
        when(mockRestTemplate.getForEntity(anyString(), eq(Map.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Verify the exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gitHubService.getRepositories("nonexistentuser"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    public void testGetRepositories_ApiError() {
        // Mock API error response
        when(mockRestTemplate.getForEntity(anyString(), eq(Map.class))).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Verify the exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gitHubService.getRepositories("user1"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error fetching user", exception.getReason());
    }

    @Test
    public void testGetRepositories_RepositoriesNotFound() {
        // Mock user response
        Map<String, Object> mockUser = new HashMap<>();
        ResponseEntity<Map> userResponse = new ResponseEntity<>(mockUser, HttpStatus.OK);
        when(mockRestTemplate.getForEntity(anyString(), eq(Map.class))).thenReturn(userResponse);

        // Mock repositories not found response
        when(mockRestTemplate.getForEntity(contains("/repos"), eq(List.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Verify the exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gitHubService.getRepositories("user1"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Error fetching repositories", exception.getReason());
    }
}
