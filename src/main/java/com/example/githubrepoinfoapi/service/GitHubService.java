package com.example.githubrepoinfoapi.service;

import com.example.githubrepoinfoapi.model.RepositoryInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GitHubService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<RepositoryInfo> getRepositories(String username) {
        String userUrl = GITHUB_API_URL + "/users/" + username;
        ResponseEntity<Map> userResponse = restTemplate.getForEntity(userUrl, Map.class);

        // dodac "user not found"

        String reposUrl = GITHUB_API_URL + "/users/" + username + "/repos/";
        ResponseEntity<List> repResponse = restTemplate.getForEntity(reposUrl, List.class);

        // dodac error !200

        List body = repResponse.getBody();


    }
}
