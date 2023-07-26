package com.ecommerce;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.ecommerce.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AppTest {

    @Mock
    APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent;

    @Mock
    Context context;

    @Mock
    LambdaLogger loggerMock;

    @BeforeEach
    public void setup() throws Exception {
        when(context.getLogger()).thenReturn(loggerMock);

        doAnswer(invocation -> {
            System.out.println((String) invocation.getArgument(0));
            return null;
        }).when(loggerMock).log(anyString());
    }


    @Test
    public void testApp() {
        // Set up the MongoDBConfig with the local MongoDB connection string

        // Create the App instance with the MongoDBConfig
        var sut = new App();
        sut.mongoDBConfig = MongoDBConfig.getInstance("mongodb://localhost:27017");
        // Call the handleRequest method with an empty string as input
        List<Product> products = sut.handleRequest(apiGatewayProxyRequestEvent, context);

        // Assert that the returned list of products has a size of 5
        assertEquals(5, products.size());
    }
}
