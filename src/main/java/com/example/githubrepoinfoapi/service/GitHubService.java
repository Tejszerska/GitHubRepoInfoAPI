package com.example.githubrepoinfoapi.service;

import com.example.githubrepoinfoapi.model.Branch;
import com.example.githubrepoinfoapi.model.RepositoryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<RepositoryInfo> getRepositories(String username) {
        String userUrl = GITHUB_API_URL + "/users/" + username;

        ResponseEntity<Map<String, Object>> userResponse;
        try {
            userResponse = restTemplate.getForEntity(userUrl, (Class<Map<String, Object>>) (Class<?>) Map.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching user");
            }
        }

        String reposUrl = GITHUB_API_URL + "/users/" + username + "/repos";
        ResponseEntity<List<Map<String, Object>>> repResponse;
        try {
            repResponse = restTemplate.getForEntity(reposUrl, (Class<List<Map<String, Object>>>) (Class<?>) List.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error fetching repositories");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching repositories");
            }
        }

        if (repResponse.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(repResponse.getStatusCode(), "Error fetching repositories");
        }

        List<Map<String, Object>> repositories = repResponse.getBody();
        List<RepositoryInfo> result = new ArrayList<>();

        if (repositories != null) {
            for (Map<String, Object> rep : repositories) {
                if (Boolean.FALSE.equals(rep.get("fork"))) {
                    String branchesUrl = rep.get("branches_url").toString().replace("{/branch}", "");
                    ResponseEntity<List<Map<String, Object>>> branchesResponse = restTemplate.getForEntity(branchesUrl, (Class<List<Map<String, Object>>>) (Class<?>) List.class);
                    List<Map<String, Object>> branches = branchesResponse.getBody();
                    List<Branch> branchList = new ArrayList<>();
                    if (branches != null) {
                        for (Map<String, Object> branch : branches) {
                            String branchName = branch.get("name").toString();
                            String lastCommitSha = ((Map<String, Object>) branch.get("commit")).get("sha").toString();
                            branchList.add(new Branch(branchName, lastCommitSha));
                        }
                    }
                    RepositoryInfo repoInfo = new RepositoryInfo(rep.get("name").toString(), ((Map<String, Object>) rep.get("owner")).get("login").toString(), branchList);
                    result.add(repoInfo);
                }
            }
        }
        return result;
    }

}
