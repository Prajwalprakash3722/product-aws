package com.ecommerce;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.ecommerce.models.Product;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, List<Product>> {


    private List<Product> products = new ArrayList<>();

    MongoDBConfig mongoDBConfig = MongoDBConfig.getInstance();


    @Override
    public List<Product> handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log(input.toString());

        try (MongoClient mongoClient = MongoClients.create(mongoDBConfig.getConnectionString())) {
            if (mongoClient != null) logger.log("Connected to MongoDB Atlas!");
            MongoDatabase database = mongoClient.getDatabase("product-service");
            logger.log("Connected to database successfully!");
            MongoCollection<Document> collection = database.getCollection("product");
            logger.log("Collection product selected successfully!");
            products.clear();
            try (MongoCursor<Document> cursor = collection.find().limit(5).iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    Product product = Product.mapDocumentToProduct(document);
                    products.add(product);
                }
            }

        } catch (MongoException e) {
            // Log the error
            logger.log("Error: " + e);
            return products;
        }

        return products;
    }
}