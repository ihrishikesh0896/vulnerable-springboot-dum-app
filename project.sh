
#!/bin/bash

# Script to create GitHub Token Validator project structure with hardcoded token
# This version does NOT include Gradle wrapper files

# Base directory name
BASE_DIR="github-token-validator"

# Hardcoded GitHub token - replace with your actual token
GITHUB_TOKEN="ghp_YourHardcodedTokenHere123456789abcdefghijklm"

# Create base directory
mkdir -p "$BASE_DIR"

# Create directory structure
mkdir -p "$BASE_DIR/src/main/java/com/example/githubvalidator/controller"
mkdir -p "$BASE_DIR/src/main/java/com/example/githubvalidator/service"
mkdir -p "$BASE_DIR/src/main/resources/static"

# Create Java application file
cat > "$BASE_DIR/src/main/java/com/example/githubvalidator/GitHubTokenValidatorApplication.java" << 'EOF'
package com.example.githubvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitHubTokenValidatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitHubTokenValidatorApplication.class, args);
    }
}
EOF

# Create controller file with hardcoded token validation
cat > "$BASE_DIR/src/main/java/com/example/githubvalidator/controller/TokenController.java" << EOF
package com.example.githubvalidator.controller;

import com.example.githubvalidator.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    
    private static final String HARDCODED_TOKEN = "${GITHUB_TOKEN}";
    
    @Autowired
    private GitHubService gitHubService;
    
    @GetMapping("/validate")
    public String validateToken() {
        boolean isValid = gitHubService.isValidToken(HARDCODED_TOKEN);
        return "The hardcoded GitHub token is " + (isValid ? "valid" : "invalid");
    }
}
EOF

# Create service file
cat > "$BASE_DIR/src/main/java/com/example/githubvalidator/service/GitHubService.java" << 'EOF'
package com.example.githubvalidator.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GITHUB_API_URL = "https://api.github.com/user";
    
    public boolean isValidToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + token);
            
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
EOF

# Create a simple index HTML that displays validation result directly
cat > "$BASE_DIR/src/main/resources/static/index.html" << 'EOF'
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GitHub Token Validator</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            line-height: 1.6;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .result {
            margin-top: 20px;
            padding: 15px;
            border-radius: 4px;
            font-size: 18px;
        }
        .loading {
            color: #555;
        }
        .valid {
            background-color: #dcffe4;
            color: #0a5f24;
        }
        .invalid {
            background-color: #ffebeb;
            color: #9c0101;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>GitHub Token Validator</h1>
        <p>This application validates a hardcoded GitHub token.</p>
        
        <div id="result" class="result loading">Checking token validity...</div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', async () => {
            const resultDiv = document.getElementById('result');
            
            try {
                const response = await fetch('/validate');
                const result = await response.text();
                
                resultDiv.textContent = result;
                resultDiv.className = 'result ' + (result.includes('valid') ? 'valid' : 'invalid');
            } catch (error) {
                resultDiv.textContent = 'Error validating token: ' + error.message;
                resultDiv.className = 'result invalid';
            }
        });
    </script>
</body>
</html>
EOF

# Create Gradle build file
cat > "$BASE_DIR/build.gradle" << 'EOF'
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.1'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
    useJUnitPlatform()
}
EOF

cat > "$BASE_DIR/settings.gradle" << 'EOF'
rootProject.name = 'github-token-validator'
EOF

echo "Project structure created successfully in $BASE_DIR directory"
echo "IMPORTANT: Replace the placeholder GitHub token in TokenController.java with your actual token"
echo "To run: cd $BASE_DIR && gradle bootRun (requires Gradle to be installed)"