
package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import java.io.IOException;
import java.util.Optional;

public class SecretUtils {

    public static String getSecret(String secretName, String secretKey) {
        return getSecret(secretName, secretKey, "ap-south-1");
    }

    public static String getSecret(String secretName, String secretKey, String regionName) {
        Region region = Region.of(regionName);

        try (SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

            GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
            String secretString = valueResponse.secretString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(secretString);

            JsonNode secretNode = rootNode.get(secretKey);
            return (secretNode != null) ? secretNode.asText() : null;
        } catch (IOException e) {
            throw new RuntimeException("Error parsing secret JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving secret from Secrets Manager: " + e.getMessage(), e);
        }
    }

    public static String getSecret(SecretsManagerClient secretsClient, String secretName) {
        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
            .secretId(secretName)
            .build();

        GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
        return valueResponse.secretString();
    }
}
