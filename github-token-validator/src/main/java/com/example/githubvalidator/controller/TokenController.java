package com.example.githubvalidator.controller;

import com.example.githubvalidator.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    
    private static final String HARDCODED_TOKEN = "ghp_YourHardcodedTokenHere123456789abcdefghijklm";
    
    @Autowired
    private GitHubService gitHubService;
    
    @GetMapping("/validate")
    public String validateToken() {
        boolean isValid = gitHubService.isValidToken(HARDCODED_TOKEN);
        return "The hardcoded GitHub token is " + (isValid ? "valid" : "invalid");
    }
}
