package com.newsdigest.newsdigest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SummarizationService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Transactional
    public String summarize(String content) {
        String apiUrl = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(apiUrl);
            request.setHeader("Authorization", "Bearer " + apiKey);
            request.setHeader("Content-Type", "application/json");

            String json = new ObjectMapper().writeValueAsString(new TextInput(content));
            request.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseString = EntityUtils.toString(response.getEntity());
                return parseSummary(responseString);
            }
        } catch (IOException e) {
            log.error("Error during summarization", e);
            return content.substring(0, Math.min(content.length(), 100)); // 단순 예시로 처음 100자 반환
        }
    }

    private String parseSummary(String jsonOutput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonOutput);
        return rootNode.path(0).path("summary_text").asText();
    }

    private static class TextInput {
        public String inputs;

        public TextInput(String inputs) {
            this.inputs = inputs;
        }
    }
}