package com.newsdigest.newsdigest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class SummarizationServiceTest {

    @Test
    public void shouldReturnSummaryWhenValidInputProvided() {
        String apiUrl = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn";
        String apiKey = "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";
        String testContent = "Russian President Vladimir Putin will mount a counteroffensive to try to retake territory in the Kursk region captured by Ukrainian troops. "
                + "Russian forces will encounter \"a difficult fight,\" Deputy CIA Director David Cohen said on Wednesday. "
                + "The ongoing conflict has raised concerns among international observers, who believe that the fighting in the region could escalate further. "
                + "This has led to discussions about the need for a ceasefire and diplomatic negotiations to resolve the situation. "
                + "Meanwhile, both sides are continuing to prepare for the upcoming military engagements.";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(apiUrl);
            request.setHeader("Authorization", "Bearer " + apiKey);
            request.setHeader("Content-Type", "application/json");
            log.info("originalContent = {}", testContent);
            String json = new ObjectMapper().writeValueAsString(new TextInput(testContent));
            request.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseString = EntityUtils.toString(response.getEntity());

                if (statusCode == 200) {
                    String summary = parseSummary(responseString);
                    log.info("Summary: {}", summary);

                    // 검증
                    assertNotNull(summary);
                    assertTrue(summary.length() > 0);
                } else {
                    log.error("Failed to summarize. HTTP status code: {}", statusCode);
                    log.error("Response: {}", responseString);
                }
            }
        } catch (IOException e) {
            log.error("Error during API call", e);
        }
    }

    private String parseSummary(String jsonOutput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonOutput);
        JsonNode summaryNode = rootNode.path(0).path("summary_text");
        if (summaryNode.isMissingNode()) {
            summaryNode = rootNode.path(0).path("generated_text");
        }
        return summaryNode.asText();
    }

    private static class TextInput {
        public String inputs;

        public TextInput(String inputs) {
            this.inputs = inputs;
        }
    }
}