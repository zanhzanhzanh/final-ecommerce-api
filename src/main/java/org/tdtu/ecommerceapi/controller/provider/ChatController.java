package org.tdtu.ecommerceapi.controller.provider;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.tdtu.ecommerceapi.service.provider.AssistantService;
import org.tdtu.ecommerceapi.service.provider.OpenAIService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final OpenAIService openAIService;
    private final AssistantService assistantService;

    @GetMapping("/embeddings")
    public void storeEmbeddings() {
        openAIService.storeEmbeddings();
    }

    @DeleteMapping("/embeddings")
    public void deleteAllEmbeddings() {
        openAIService.deleteAllEmbeddings();
    }
//
//    @GetMapping
//    public String chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
//        return openAIService.processUserQuery(message);
//    }
//
//
//    @GetMapping("/query")
//    public Flux<String> generateQuery(@RequestParam(value = "message", defaultValue = "Hello") String message) {
//        return openAIService.processUserQueryByMongoTemplate(message);
//    }

    /**
     * Chat with the assistant using OpenAI's API.
     * @param request The chat request containing the message and thread ID.
     * @return The response from the assistant.
     */
    @PostMapping("/assistant")
    public AssistantService.ChatResponse chat(@RequestBody ChatRequest request) {
        return assistantService.chat(request.getMessage(), request.getThreadId());
    }

    @Data
    public static class ChatRequest {
        private String message;
        private String threadId;
    }
}
