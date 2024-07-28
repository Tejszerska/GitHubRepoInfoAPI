package com.example.githubrepoinfoapi.service;

import com.example.githubrepoinfoapi.model.Branch;
import com.example.githubrepoinfoapi.model.RepositoryInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<RepositoryInfo> getRepositories(String username) {
        String userUrl = GITHUB_API_URL + "/users/" + username;
        ResponseEntity<Map> userResponse = restTemplate.getForEntity(userUrl, Map.class);

        if (userResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        String reposUrl = GITHUB_API_URL + "/users/" + username + "/repos";
        ResponseEntity<List> repResponse = restTemplate.getForEntity(reposUrl, List.class);

        if (repResponse.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(repResponse.getStatusCode(), "Error fetching repositories");
        }

        List<Map<String, Object>> repositories = repResponse.getBody();
        List<RepositoryInfo> result = new ArrayList<>();

        if (repositories != null) {
            for (Map<String, Object> rep : repositories) {
                if (Boolean.FALSE.equals(rep.get("fork"))) { // Poprawienie sprawdzenia na false
                    String branchesUrl = rep.get("branches_url").toString().replace("{/branch}", "");
                    ResponseEntity<List> branchesResponse = restTemplate.getForEntity(branchesUrl, List.class);
                    List<Map<String, Object>> branches = branchesResponse.getBody();
                    List<Branch> branchList = new ArrayList<>();
                    if (branches != null) {
                        for (Map<String, Object> branch : branches) {
                            String branchName = branch.get("name").toString();
                            String lastCommitSha = ((Map) branch.get("commit")).get("sha").toString();
                            branchList.add(new Branch(branchName, lastCommitSha));
                        }
                    }
                    RepositoryInfo repoInfo = new RepositoryInfo(rep.get("name").toString(), ((Map) rep.get("owner")).get("login").toString(), branchList);
                    result.add(repoInfo);
                }
            }
        }
        return result;
    }
}
