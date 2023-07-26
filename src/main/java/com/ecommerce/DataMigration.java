package com.ecommerce;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataMigration {

    public static void main(String[] args) throws IOException {
        String atlasConnectionString = MongoDBConfig.getInstance().getConnectionString();

        try (MongoClient atlasClient = MongoClients.create(atlasConnectionString)) {
            System.out.println("Connected to MongoDB Atlas!");

            MongoDatabase atlasDatabase = atlasClient.getDatabase("product-service");
            MongoCollection<Document> atlasCollection = atlasDatabase.getCollection("product");

            int requestCount = 10; // Example: Change this to the desired number of requests, this sends 1000 product objects to the DB
            int productsPerRequest = 100;

            // Create a thread pool with the desired number of threads
            ExecutorService executorService = Executors.newFixedThreadPool(requestCount);

            for (int i = 0; i < requestCount; i++) {
                // Submit each data migration task to the thread pool
                executorService.submit(() -> {
                    try {
                        // Request data from the API and convert it to a List of Documents
                        List<Document> data = fetchDataFromAPI(productsPerRequest);

                        // Insert the fetched data to MongoDB
                        atlasCollection.insertMany(data.get(0).getList("products", Document.class));

                    } catch (IOException e) {
                        System.err.println("Error during data migration: " + e);
                    }
                });
            }

            executorService.shutdown();

            System.out.println("Data migration completed!");

        } catch (MongoException e) {
            System.err.println("Error during data migration: " + e);
        }
    }

    private static List<Document> fetchDataFromAPI(int limit) throws IOException {
        String apiUrl = "https://dummyjson.com/products?limit=" + limit + "&skip=0";
        URL url = new URL(apiUrl);

        // Open an HTTP connection
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Get the response code
        int responseCode = con.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response into a List of Documents
            return Collections.singletonList(Document.parse(response.toString()));
        } else {
            throw new IOException("Failed to fetch data from the API. Response code: " + responseCode);
        }
    }
}
