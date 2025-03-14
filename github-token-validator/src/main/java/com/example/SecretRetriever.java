package com.example;

import com.example.SecretsManagerApp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import java.io.IOException;
import java.util.Optional;
 
public class SecretRetriever {

    public static String retrieveSecretValue(String secretName, String secretKey, String regionName) throws IOException {
        Region region = Region.of(regionName);
    
        try (SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {
    
            Optional<String> secretStringOptional = SecretsManagerApp.getSecret(secretsClient, secretName);
    
            if (secretStringOptional.isPresent()) {
                String secretString = secretStringOptional.get();
    
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(secretString);
    
                JsonNode secretNode = rootNode.get(secretKey);
                return (secretNode != null) ? secretNode.asText() : null;
            }
        }
        return null;
    }
}
