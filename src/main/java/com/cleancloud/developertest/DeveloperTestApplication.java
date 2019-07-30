package com.cleancloud.developertest;

import com.google.cloud.bigquery.*;
import com.google.cloud.bigquery.QueryResponse;
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

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DeveloperTestApplication.class, args);

        // [START bigquery_simple_app_client]
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        // [END bigquery_simple_app_client]
        // [START bigquery_simple_app_query]
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                        "SELECT "
                                + "CONCAT('https://stackoverflow.com/questions/', CAST(id as STRING)) as url, "
                                + "view_count "
                                + "FROM `bigquery-public-data.stackoverflow.posts_questions` "
                                + "WHERE tags like '%google-bigquery%' "
                                + "ORDER BY favorite_count DESC LIMIT 10")
                        // Use standard SQL syntax for queries.
                        // See: https://cloud.google.com/bigquery/sql-reference/
                        .setUseLegacySql(false)
                        .build();

        // Create a job ID so that we can safely retry.
        JobId jobId = JobId.of("asdf");
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        queryJob = queryJob.waitFor();

        // Check for errors
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        // [END bigquery_simple_app_query]

        // [START bigquery_simple_app_print]
        // Get the results.
        TableResult result = queryJob.getQueryResults();

        // Print all pages of the results.
        for (FieldValueList row : result.iterateAll()) {
            String url = row.get("url").getStringValue();
            long viewCount = row.get("view_count").getLongValue();
            System.out.printf("url: %s views: %d%n", url, viewCount);
        }
        // [END bigquery_simple_app_print]
    }

//    @Bean
//    DynamoDbClient dynamoDbClient() {
//        return DynamoDbClient.create();
//    }
//
//    @Bean
//    ApplicationRunner applicationRunner(DynamoDbClient dynamoDbClient) {
//        return args -> {
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
//
//            HashMap<String, AttributeValue> key_to_get =
//                    new HashMap<String, AttributeValue>();
//
//            key_to_get.put("name", AttributeValue.builder()
//                    .s("name").build());
//
//            DeleteItemRequest request = DeleteItemRequest.builder()
//                    .key(key_to_get)
//                    .tableName("mateusserejo_github")
//                    .build();
//
//            DynamoDbClient ddb = DynamoDbClient.create();
//            ddb.deleteItem(request);
//        }
//    }
}
