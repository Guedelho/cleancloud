package com.cleancloud.developertest;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class DeveloperTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeveloperTestApplication.class, args);
    }

    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.create();
    }

    @Bean
    ApplicationRunner applicationRunner(DynamoDbClient dynamoDbClient) {
        return args -> {
//			HashMap<String,AttributeValue> item_values =
//					new HashMap<String,AttributeValue>();
//
//			item_values.put("name", AttributeValue.builder().s("name").build());
//			item_values.put("url", AttributeValue.builder().s("url").build());
//			item_values.put("language", AttributeValue.builder().s("language").build());
//			item_values.put("total_mega_bytes", AttributeValue.builder().n("1234").build());
//
//			PutItemRequest request = PutItemRequest.builder()
//					.tableName("mateusserejo_github")
//					.item(item_values)
//					.build();
//			DynamoDbClient ddb = DynamoDbClient.create();
//			ddb.putItem(request);

            HashMap<String, AttributeValue> key_to_get =
                    new HashMap<String, AttributeValue>();

            key_to_get.put("name", AttributeValue.builder()
                    .s("name").build());

            DeleteItemRequest request = DeleteItemRequest.builder()
                    .key(key_to_get)
                    .tableName("mateusserejo_github")
                    .build();

            DynamoDbClient ddb = DynamoDbClient.create();
            ddb.deleteItem(request);

        };
    }
}
