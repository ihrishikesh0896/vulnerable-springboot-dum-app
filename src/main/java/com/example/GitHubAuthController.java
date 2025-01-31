package main.java.com.example;

import org.kohsuke.github.GitHub;
import org.springframework.web.bind.annotation.*;

@RestController
public class GitHubAuthController {

    private static final String GITHUB_TOKEN = "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";

    @PostMapping("/authenticate")
    public String authenticateToken() {
        try {
            GitHub github = GitHub.connectUsingOAuth(GITHUB_TOKEN);
            if (github.isCredentialValid()) {
                return "Token is valid";
            } else {
                return "Token is invalid";
            }
        } catch (Exception e) {
            return "Error authenticating token: " + e.getMessage();
        }
    }
}