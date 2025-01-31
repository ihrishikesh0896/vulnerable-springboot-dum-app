from fix_secrets import secrets_remediation
package main.java.com.example;

import org.kohsuke.github.GitHub;
import org.springframework.web.bind.annotation.*;

@RestController
public class GitHubAuthController {

    private static final String GITHUB_TOKEN = secrets_remediation.get_secret("vulnerable-springboot-dum-app","Replaced_c1facaf4");

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