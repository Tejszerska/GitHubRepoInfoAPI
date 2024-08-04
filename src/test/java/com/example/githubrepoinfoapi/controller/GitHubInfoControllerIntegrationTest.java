package com.example.githubrepoinfoapi.controller;

import com.example.githubrepoinfoapi.model.RepositoryInfo;
import com.example.githubrepoinfoapi.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitHubInfoController.class)
public class GitHubInfoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Test
    public void testGetRepositories_Success() throws Exception {
        List<RepositoryInfo> mockRepositories = Collections.singletonList(new RepositoryInfo("repo1", "user1", Collections.emptyList()));
        when(gitHubService.getRepositories(anyString())).thenReturn(mockRepositories);

        mockMvc.perform(get("/ghinfo/user1")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"repositoryName\":\"repo1\",\"ownerLogin\":\"user1\",\"branches\":[]}]"));
    }

    @Test
    public void testGetRepositories_InvalidAcceptHeader() throws Exception {
        mockMvc.perform(get("/ghinfo/user1")
                        .header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotAcceptable());
    }
}
