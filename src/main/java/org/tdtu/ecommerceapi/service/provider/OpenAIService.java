package org.tdtu.ecommerceapi.service.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.response.ProductResDto;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.model.rest.Promotion;
import org.tdtu.ecommerceapi.repository.CategoryRepository;
import org.tdtu.ecommerceapi.repository.ProductRepository;
import org.tdtu.ecommerceapi.repository.PromotionRepository;
import org.tdtu.ecommerceapi.utils.MappingUtils;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {
    private final EmbeddingModel embedding;
    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final MongoTemplate mongoTemplate;
    private final OpenAiChatModel chatModel;
    private final MappingUtils mappingUtils;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;

    /**
     * Delete all embeddings from the vector store.
     * This method should be called to clear the vector store before re-initializing it with new data.
     */
    public void deleteAllEmbeddings() {
        vectorStore.delete(new Filter.Expression(
                Filter.ExpressionType.IN,
                new Filter.Key("entityType"),
                new Filter.Value(List.of("Product", "Category", "Promotion"))));
    }

    /**
     * Store embeddings for products, categories, and promotions in the vector store.
     * This method should be called to initialize the vector store with the necessary data.
     */
    public void storeEmbeddings() {
        // Product
        List<Product> products = productRepository.findAll();
        List<Document> productDocs = products.stream().map(product -> {
            String content = String.format(
                    "Product: %s," +
                            " Category: %s," +
                            " Description: %s" +
                            " Quantity: %d" +
                            " Price: %.2f",
                    product.getProductName(),
                    product.getCategory().getId(),
                    product.getDescription(),
                    product.getQuantity(),
                    product.getPrice());
            float[] embeddingVector = embedding.embed(content);

            return new Document(Map.of(
                    "id", product.getId(),
                    "content", content,
                    "embedding", embeddingVector
            ).toString(),
                    Map.of(
                            "entityType", "Product",
                            "entityId", product.getId().toString(),
                            "categoryId", product.getCategory().getId().toString()
                    ));
        }).collect(Collectors.toList());
        vectorStore.add(productDocs);

        // Category
        List<Category> categories = categoryRepository.findAll();
        List<Document> categoryDocs = categories.stream().map(category -> {
            StringBuilder content = new StringBuilder(String.format(
                    "Category: %s,\n",
                    category.getCategoryName()));

            category.getProducts().forEach(product -> {
                content.append(String.format(
                        "Product: %s, Description: %s, Price: %.2f\n",
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice()
                ));
            });

            float[] embeddingVector = embedding.embed(content.toString());

            return new Document(Map.of(
                    "id", category.getId(),
                    "content", content.toString(),
                    "embedding", embeddingVector
            ).toString(),
                    Map.of(
                            "entityType", "Category",
                            "entityId", category.getId().toString()
                    ));
        }).collect(Collectors.toList());
        vectorStore.add(categoryDocs);

        // Promotion
        List<Promotion> promotions = promotionRepository.findAll();
        List<Document> promotionDocs = promotions.stream().map(promotion -> {
            String content = String.format(
                    "Promotion: %s," +
                            " Description: %s," +
                            " Discount Amount: %.2f," +
                            " Promotion Type: %s," +
                            " Proportion Type: %s," +
                            " Min Order Value: %.2f," +
                            " Product IDs: %s",
                    promotion.getPromotionName(),
                    promotion.getDescription(),
                    promotion.getDiscountAmount(),
                    promotion.getPromotionType(),
                    promotion.getProportionType(),
                    promotion.getMinOrderValue(),
                    promotion.getProductIds().stream()
                            .map(UUID::toString)
                            .collect(Collectors.joining(", ")));
            float[] embeddingVector = embedding.embed(content);

            return new Document(Map.of(
                    "id", promotion.getId(),
                    "content", content,
                    "embedding", embeddingVector
            ).toString(),
                    Map.of(
                            "entityType", "Promotion",
                            "entityId", promotion.getId().toString(),
                            "startDate", promotion.getStartDate(),
                            "endDate", promotion.getEndDate(),
                            "discountAmount", promotion.getDiscountAmount(),
                            "promotionType", promotion.getPromotionType().toString(),
                            "proportionType", promotion.getProportionType().toString(),
                            "productIds", promotion.getProductIds().stream()
                                    .map(UUID::toString)
                                    .collect(Collectors.joining(", "))
                    ));
        }).collect(Collectors.toList());
        vectorStore.add(promotionDocs);
    }

    /**
     * Process a user query by searching for relevant documents in the vector store
     * and generating a response based on the retrieved context.
     *
     * @param query The user query to process.
     * @return A response string generated from the context of relevant documents.
     */
    public String processUserQuery(String query) {
        try {
            List<Document> relevantDocs = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(query)
                            .topK(5)
//                        .similarityThreshold(0.7)
                            .build());

            StringBuilder context = new StringBuilder();
            for (Document doc : relevantDocs) {
                String entityType = doc.getMetadata().get("entityType").toString();
                String entityId = doc.getMetadata().get("entityId").toString();

                if ("Product".equals(entityType)) {
                    context.append(doc.getText()).append("\n");
                } else if ("Category".equals(entityType)) {
                    context.append(doc.getText()).append("\n");
                } else if ("Promotion".equals(entityType)) {
                    context.append(doc.getText()).append("\n");

                    // Load associated products for the promotion
                    String productIdsString = doc.getMetadata().get("productIds").toString();
                    List<UUID> productIds = productIdsString.isEmpty() ? List.of() :
                            Stream.of(productIdsString.split(", "))
                                    .map(UUID::fromString)
                                    .collect(Collectors.toList());

                    List<Product> products = productRepository.findAllById(productIds);
                    PromotionType promotionType = PromotionType.valueOf(doc.getMetadata().get("promotionType").toString());
                    ProportionType proportionType = ProportionType.valueOf(doc.getMetadata().get("proportionType").toString());
                    double discountAmount = Double.parseDouble(doc.getMetadata().get("discountAmount").toString());

                    products.forEach(product -> {
                        double newPrice = product.getPrice();
                        if (PromotionType.SPECIFIC_PRODUCTS.equals(promotionType)) {
                            if (ProportionType.PERCENTAGE.equals(proportionType)) {
                                newPrice -= newPrice * (discountAmount / 100);
                            } else if (ProportionType.ABSOLUTE.equals(proportionType)) {
                                newPrice -= discountAmount;
                            }
                        }
                        context.append(String.format(
                                "Product: %s, Description: %s, Original Price: %.2f, New Price: %.2f\n",
                                product.getProductName(),
                                product.getDescription(),
                                product.getPrice(),
                                newPrice
                        ));
                    });
                } else {
                    log.warn("Unknown entity type: {}", entityType);
                }
            }

//        String prompt = "Based on the following information, answer the question: " + query + "\n\n" + context;
//        String prompt = "To answer this query: \"" + query + "\", use this object information:\n" + context + "\n" +
//                "First, identify any numerical conditions in the query, such as 'price less than 1000' or 'quantity greater than 5'." +
//                " Then, only consider objects that meet those conditions when formulating your answer. If no objects meet the conditions," +
//                " please state that no matching objects were found.";
//        String prompt = "To answer this query: \"" + query + "\", use the following objects information:\n" + context + "" +
//                "\nFirst, for each object in the information, extract all numerical fields (such as quantity,price,...) as a numerical value." +
//                " For example, from 'Product: Tablet, Category: Electronics, Description: Portable tablet Quantity: 75 Price: 1560000.00'," +
//                " extract name = 'Tablet', category = 'Electronics', description = 'Portable tablet', quantity = 75, price = 1560000.00.\n" +
//                "Next, identify all conditions in the query, including numerical conditions such as 'price less than 65000' or 'quantity greater than 5'," +
//                " and non-numerical conditions such as 'category is Electronics'.\n" +
//                "Then, filter the objects to only include those that satisfy all conditions specified in the query." +
//                " For numerical conditions, ensure numerical values are compared correctly (e.g., 1560000.00 is not less than 65000).\n" +
//                "Finally, just only list all object that meet all conditions, or state that no objects meet the conditions if that is the case.";
            String prompt = "To answer this query: \"" + query + "\", use the following objects information:\n" + context +
                    "\nWe will rely on the provided objects information to answer, except if the user asks a question related to a numerical condition query," +
                    " verify the object information for fields related to numbers." +
                    " If no objects are found, just only respond with no results found.\n" +
                    " For example, the following object information:\n" +
                    "{embedding=[F@324dc3c, id=958f3d71-0049-4e2b-87b3-e41f3ba0ce56, content=Product: Smartphone, Category: 89d31394-f428-4696-94a0-3a8d147eff76, Description: Latest smartphone Quantity: 100 Price: 2000000.00}\n" +
                    "{embedding=[F@7937abb8, id=74bf0f9a-7a42-4fcc-ab16-4c827d71c79e, content=Product: Laptop, Category: 89d31394-f428-4696-94a0-3a8d147eff76, Description: High-performance laptop Quantity: 50 Price: 5600000.00}\n" +
                    "{embedding=[F@73199c82, id=dc7a8552-a378-4b63-82a7-734dc2cddccf, content=Product: Tablet, Category: 89d31394-f428-4696-94a0-3a8d147eff76, Description: Portable tablet Quantity: 75 Price: 1560000.00}\n" +
                    "{embedding=[F@7c1aadab, id=89d31394-f428-4696-94a0-3a8d147eff76, content=Category: Electronics,}\n" +
                    "{embedding=[F@275199cc, id=f69a0039-4110-4bb2-96e2-8e494e2a229a, content=Product: Programming Book, Category: 331f66a7-f155-4fbf-b086-5719847c697f, Description: Learn programming Quantity: 200 Price: 65000.00}\n" +
                    "User query: Give me all products with price less than 65000 related to electronics\n" +
                    "The answer is: No results found.\n" +
                    "Another User query: Give me all products with price equal than 65000 related to books\n" +
                    "You will answer the object Programming Book with full information\n";

            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("Error processing user query: {}", e.getMessage());
            return "An error occurred while processing your query. Please try again later.";
        }
    }

    /**
     * Process a user query using MongoDB template to generate a query or answer.
     *
     * @param message The user query message.
     * @return A Flux of strings containing the results or answers.
     */
    public Flux<String> processUserQueryByMongoTemplate(String message) {
        String prompt = """
                You are an assistant that helps users query a MongoDB database using natural language or answer general knowledge questions directly.

                The database has the following collections:

                - categories:
                  _id (UUID): This is the unique identifier.
                  categoryName (string): This is the name of the category.
                - products:
                  _id (UUID): This is the unique identifier.
                  productName (string): The name of the product. Must not be blank and must contain at least 5 characters.
                  image (string): The URL or path to the product image.
                  description (string): A description of the product. Must not be blank and must contain at least 5 characters.
                  quantity (integer): The available quantity of the product. Must be zero or a positive number.
                  price (double): The price of the product. Must be a positive number.
                  category (Category): A reference to a Category document, lazily loaded.
                - promotions:
                  promotionName (string): The name of the promotion.
                  promotionCode (string): A unique code for the promotion.
                  description (string): A description of the promotion.
                  startDate (OffsetDateTime): The start date and time of the promotion.
                  endDate (OffsetDateTime): The end date and time of the promotion.
                  discountAmount (Double): The discount value for the promotion.
                  promotionType (PromotionType): The type of promotion, stored as an enumerated string.
                    PromotionType
                      There are three types of promotionType:
                        ALL_PRODUCTS: Applies a discount to all products.
                          Cannot use proportionType = ABSOLUTE.
                          Does not use minOrderValue.
                          Does not include productIds.
                        ORDER_TOTAL: Applies a discount to the total order value.
                          Does not include productIds.
                        SPECIFIC_PRODUCTS: Applies a discount to a specific group of products.
                          Does not use minOrderValue.
                  proportionType (ProportionType): The type of proportion for the promotion, stored as an enumerated string.
                    proportionType
                      There are two types of proportionType:
                        PERCENTAGE: The discount value is applied as a percentage.
                        ABSOLUTE: The discount value is a fixed amount subtracted directly.
                  minOrderValue (Double): The minimum order value required for the promotion to apply.
                  productIds (Set<uuid>): A set of unique identifiers for products associated with the promotion.

                Your task is to determine if the user's query is related to the database or if it's a general knowledge question.

                - If the query is related to the database (e.g., about categories, products, or promotions), generate a MongoDB query as a JSON object with the following structure:
                  {
                    "type": "query",
                    "collection": "the collection name",
                    "query": the MongoDB query or pipeline
                  }

                - If the query is a general knowledge question (e.g., "What is the capital of France?"), provide a direct answer as a JSON object with the following structure:
                  {
                    "type": "answer",
                    "text": "the answer to the question"
                  }

                For example:
                - For 'Show me all products in the electronics category that are on promotion':
                  {
                    "type": "query",
                    "collection": "products",
                    "query": [
                      {
                        "$lookup": {
                          "from": "categories",
                          "let": { "productCategory": "$category" },
                          "pipeline": [
                            {
                              "$match": {
                                "$expr": {
                                  "$and": [
                                    { "$eq": ["$_id", "$$productCategory"] },
                                    { "$eq": ["$categoryName", "Electronics"] }
                                  ]
                                }
                              }
                            }
                          ],
                          "as": "categoryMatch"
                        }
                      },
                      {
                        "$match": {
                          "categoryMatch.0": { "$exists": true }
                        }
                      },
                      {
                        "$lookup": {
                          "from": "promotions",
                          "let": { "productId": "$_id" },
                          "pipeline": [
                            {
                              "$match": {
                                "$expr": {
                                  "$and": [
                                    { "$lte": ["$startDate.dateTime", { "$date": "CURRENT_DATE_TIME" }] },
                                    { "$gte": ["$endDate.dateTime", { "$date": "CURRENT_DATE_TIME" }] },
                                    { "$in": ["$$productId", "$productIds"] }
                                  ]
                                }
                              }
                            }
                          ],
                          "as": "activePromotions"
                        }
                      },
                      {
                        "$match": {
                          "activePromotions.0": { "$exists": true }
                        }
                      },
                      {
                        "$project": {
                          "categoryMatch": 0,
                          "activePromotions": 0
                        }
                      }
                    ]
                  }

                - For 'What is the capital of France?':
                  {
                    "type": "answer",
                    "text": "The capital of France is Paris."
                  }

                Now, for the following query: "%s"

                Return the appropriate JSON object based on whether it's a database query or a general knowledge question.
                """.formatted(message);

        try {
            String aiResponse = chatModel.call(prompt);
            aiResponse = aiResponse.replace("\"CURRENT_DATE_TIME\"", "\"" + OffsetDateTime.now() + "\"");

            org.bson.Document responseDoc = org.bson.Document.parse(aiResponse.trim());
            String type = responseDoc.getString("type");

            if ("query".equals(type)) {
                // Handle database query
                String collection = responseDoc.getString("collection");
                Object queryObj = responseDoc.get("query");

                List<?> results;
                if (queryObj instanceof List) {
                    // Aggregation pipeline
                    List<org.bson.Document> pipeline = (List<org.bson.Document>) queryObj;
                    List<AggregationOperation> operations = pipeline.stream()
                            .map(OpenAIService.CustomAggregationOperation::new)
                            .collect(Collectors.toList());
                    Aggregation aggregation = Aggregation.newAggregation(operations);
                    AggregationResults<org.bson.Document> aggResults = mongoTemplate.aggregate(aggregation, collection, org.bson.Document.class);
                    results = aggResults.getMappedResults();
                } else if (queryObj instanceof org.bson.Document) {
                    // Simple find query
                    org.bson.Document queryDoc = (org.bson.Document) queryObj;
                    switch (collection) {
                        case "products":
                            results = mongoTemplate.find(new BasicQuery(queryDoc), Product.class);
                            break;
                        case "categories":
                            results = mongoTemplate.find(new BasicQuery(queryDoc), Category.class);
                            break;
                        case "promotions":
                            results = mongoTemplate.find(new BasicQuery(queryDoc), Promotion.class);
                            break;
                        default:
                            return Flux.just("Unknown collection: " + collection);
                    }
                } else {
                    return Flux.just("Invalid query type");
                }

                // Map results to strings
                return Flux.fromIterable(results)
                        .map(obj -> {
                            if (obj instanceof Product) {
                                return ((Product) obj).getProductName() + ",";
                            } else if (obj instanceof Category) {
                                return ((Category) obj).getCategoryName() + ",";
                            } else if (obj instanceof Promotion) {
                                return ((Promotion) obj).getPromotionName() + ",";
                            } else if (obj instanceof org.bson.Document) {
                                org.bson.Document doc = (org.bson.Document) obj;
                                if (collection.equals("products")) {
                                    return doc.getString("productName") + ",";
                                } else if (collection.equals("categories")) {
                                    return doc.getString("categoryName") + ",";
                                } else if (collection.equals("promotions")) {
                                    return doc.getString("promotionName") + ",";
                                } else {
                                    return doc.toString();
                                }
                            } else {
                                return obj.toString();
                            }
                        });
            } else if ("answer".equals(type)) {
                // Handle direct answer
                String answer = responseDoc.getString("text");
                return Flux.just(answer);
            } else {
                return Flux.just("Invalid response type");
            }
        } catch (Exception e) {
            log.error("Error processing chat request: {}", e.getMessage(), e);
            return Flux.just("We cannot process your request at the moment. Please try again later.");
        }
    }

    private static class CustomAggregationOperation implements AggregationOperation {
        private final org.bson.Document operation;

        public CustomAggregationOperation(org.bson.Document operation) {
            this.operation = operation;
        }

        @Override
        public org.bson.Document toDocument(AggregationOperationContext context) {
            return operation;
        }
    }

    /**
     * Find similar products based on the given product ID.
     * This method retrieves the product, constructs a content string, and performs a similarity search in the vector store.
     *
     * @param productId The UUID of the product to find similar products for.
     * @return A list of similar products.
     */
    public List<ProductResDto> findSimilarProducts(UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        String content = getContent(productId, optionalProduct);

//        Filter.Expression filter = new Filter.Expression(
//                Filter.ExpressionType.EQ,
//                new Filter.Key("categoryId"),
//                new Filter.Value(product.getCategory().getId())
//        );

        List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(content)
                        .topK(20)
                        // Create Vector Search Index if use this
//                        .filterExpression(filter)
                        .build()
        );

        List<UUID> similarProductIds = similarDocs.stream()
                .filter(doc -> "Product".equals(doc.getMetadata().get("entityType")))
                .map(doc -> UUID.fromString(doc.getMetadata().get("entityId").toString()))
                .filter(id -> !id.equals(productId))
                .collect(Collectors.toList());

        return mappingUtils.mapListToDTO(productRepository.findAllById(similarProductIds), ProductResDto.class);
    }

    private static String getContent(UUID productId, Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(Product.class, "id", productId.toString());
        }
        Product product = optionalProduct.get();

        return String.format(
                "Product: %s," +
                        " Category: %s," +
                        " Description: %s" +
                        " Quantity: %d" +
                        " Price: %.2f",
                product.getProductName(),
                product.getCategory().getId(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice()
        );
    }
}
