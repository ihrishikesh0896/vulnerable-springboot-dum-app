package com.example.githubvalidator.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.SecretRetriever;

@Service
public class GitHubService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GITHUB_API_URL = "https://api.github.com/user";
    private String githubToken;

    public GitHubService() {
        try {
            this.githubToken = SecretRetriever.retrieveSecretValue(
                "vulnerable-springboot-dum-app__", 
                "Replaced_fe1a2805", 
                "ap-south-1"
            );
            System.out.println("Secret Token Retrieved");
        } catch (IOException e) {
            System.err.println("Error retrieving secret: " + e.getMessage());
            e.printStackTrace();
            this.githubToken = null; // Ensure it’s not left uninitialized
        }
    }
    
    public boolean isValidToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + githubToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                GITHUB_API_URL, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
