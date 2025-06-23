package org.tdtu.ecommerceapi.service.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.tdtu.ecommerceapi.exception.NotFoundException;

import java.util.List;

@Service
public class AssistantService {
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String assistantId;
    private final String apiUrl;

    public AssistantService(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${openai.assistant-id}") String assistantId,
            @Value("${openai.api-url}") String apiUrl) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
        this.assistantId = assistantId;
        this.apiUrl = apiUrl;
    }

    public ChatResponse chat(String userMessage, String threadId) {
        try {
            if (threadId == null || threadId.isEmpty()) {
                threadId = createThread();
            }

            createMessage(threadId, userMessage);

            String runId = createRun(threadId);

            return new ChatResponse(pollForResponse(threadId, runId), threadId);
        }
        catch (Exception e) {
            if(e instanceof HttpClientErrorException) {
                throw new NotFoundException(ThreadResponse.class, "id", threadId);
            }
            return new ChatResponse("Server error. Please try again later!", threadId);
        }
    }

    private String createThread() {
        String url = apiUrl + "/threads";
        HttpHeaders headers = getHeaders();
        HttpEntity<String> request = new HttpEntity<>("{}", headers);
        ThreadResponse response = restTemplate.postForObject(url, request, ThreadResponse.class);
        return response.getId();
    }

    private void createMessage(String threadId, String userMessage) {
        String url = apiUrl + "/threads/" + threadId + "/messages";
        HttpHeaders headers = getHeaders();
        String requestBody = String.format("""
            {
                "role": "user",
                "content": "%s"
            }
            """, userMessage.replace("\"", "\\\""));
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        restTemplate.postForObject(url, request, String.class);
    }

    private String createRun(String threadId) {
        String url = apiUrl + "/threads/" + threadId + "/runs";
        HttpHeaders headers = getHeaders();
        String requestBody = String.format("""
            {
                "assistant_id": "%s"
            }
            """, assistantId);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        RunResponse response = restTemplate.postForObject(url, request, RunResponse.class);
        return response.getId();
    }

    private String pollForResponse(String threadId, String runId) {
        String runUrl = apiUrl + "/threads/" + threadId + "/runs/" + runId;
        HttpHeaders headers = getHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        while (true) {
            RunResponse runResponse = restTemplate.exchange(runUrl, HttpMethod.GET, request, RunResponse.class).getBody();
            if ("completed".equals(runResponse.getStatus())) {
                break;
            } else if ("failed".equals(runResponse.getStatus()) || "cancelled".equals(runResponse.getStatus())) {
                return "Error: Run " + runResponse.getStatus();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error: Interrupted while polling";
            }
        }

        String messagesUrl = apiUrl + "/threads/" + threadId + "/messages";
        MessageListResponse messages = restTemplate.exchange(messagesUrl, HttpMethod.GET, request, MessageListResponse.class).getBody();

        for (Message message : messages.getData()) {
            if ("assistant".equals(message.getRole())) {
                return message.getContent().get(0).getText().getValue();
            }
        }
        return "No response from Assistant";
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.set("OpenAI-Beta", "assistants=v2");
        return headers;
    }

    @Data
    @AllArgsConstructor
    public static class ChatResponse {
        private String message;
        private String threadId;
    }

    @Data
    static class ThreadResponse {
        private String id;
    }

    @Data
    static class RunResponse {
        private String id;
        private String status;
    }

    @Data
    static class MessageListResponse {
        private List<Message> data;
    }

    @Data
    static class Message {
        private String role;
        private List<Content> content;
    }

    @Data
    static class Content {
        private Text text;
    }

    @Data
    static class Text {
        private String value;
    }
}
