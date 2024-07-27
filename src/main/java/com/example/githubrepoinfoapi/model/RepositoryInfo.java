package com.example.githubrepoinfoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryInfo {
    private String repositoryName;
    private String ownerLogin;
    private List<Branch> branches;
}