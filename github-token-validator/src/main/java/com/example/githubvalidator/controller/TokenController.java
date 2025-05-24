package com.example.githubvalidator.controller;

import com.example.SecretUtils;

import com.example.githubvalidator.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    
    private static final String HARDCODED_TOKEN = "SecretUtils.getSecret("vulnerable-springboot-dum-app__", "Replaced_300c786c", "ap-south-1", true)fghijklm";
    
    @Autowired
    private GitHubService gitHubService;
    
    @GetMapping("/validate")
    public String validateToken() {
        boolean isValid = gitHubService.isValidToken(HARDCODED_TOKEN);
        return "The hardcoded GitHub token is " + (isValid ? "valid" : "invalid");
    }
}
