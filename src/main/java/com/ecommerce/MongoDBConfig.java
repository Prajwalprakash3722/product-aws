package com.ecommerce;

public class MongoDBConfig {
    private static final MongoDBConfig instance = new MongoDBConfig();

    private String connectionString;

    private MongoDBConfig() {
        // Initialize the connection string using the system environment variables
        // If the environment variable is not set, use the default value
        this.connectionString = System.getenv("MONGODB_CONNECTION_STRING");
    }

    private MongoDBConfig(String connectionString) {
        this.connectionString = connectionString;
    }

    public static MongoDBConfig getInstance() {
        return instance;
    }

    public static MongoDBConfig getInstance(String connectionString) {
        return new MongoDBConfig(connectionString);
    }


    public String getConnectionString() {
        return connectionString;
    }
}
