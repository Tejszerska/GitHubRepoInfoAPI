package com.example.githubrepoinfoapi.controller;

import com.example.githubrepoinfoapi.model.RepositoryInfo;
import com.example.githubrepoinfoapi.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/ghinfo")
@RequiredArgsConstructor
public class GitHubInfoController {
    private GitHubService gitHubService;

    @GetMapping("/{username}")
    public List<RepositoryInfo> getRepositories(@PathVariable String username, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {

        return gitHubService.getRepositories(username);
    }
}
