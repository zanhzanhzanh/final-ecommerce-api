package org.tdtu.ecommerceapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class OpenAIConfig {
    private final OpenAiChatModel openAiChatModel;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean
    public VectorStore vectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        return MongoDBAtlasVectorStore.builder(mongoTemplate, embeddingModel)
                .initializeSchema(true)
                .build();
    }

//    @Bean
//    public EmbeddingModel embeddingModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
//        return new OpenAiEmbeddingModel(
//                OpenAiApi.builder()
//                        .apiKey(apiKey)
//                        .build(),
//                MetadataMode.EMBED,
//                OpenAiEmbeddingOptions.builder()
//                        .model("text-embedding-ada-002")
//                        .build(),
//                RetryUtils.DEFAULT_RETRY_TEMPLATE
//        );
//    }
}
