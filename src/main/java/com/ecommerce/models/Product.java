package com.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Rating rating;


    @Data
    public static class Rating {
        private double rate;
        private double count;
    }

    public static Document mapProductToDocument(Product product) {
        Document document = new Document();
        document.append("title", product.getTitle());
        document.append("price", product.getPrice());
        document.append("description", product.getDescription());
        document.append("category", product.getCategory());
        document.append("imageUrl", product.getImageUrl());
        document.append("createdAt", product.getCreatedAt());
        document.append("updatedAt", product.getUpdatedAt());

        if (product.getId() != null && !product.getId().isEmpty()) {
            document.append("_id", new org.bson.types.ObjectId(product.getId()));
        }

        if (product.getRating() != null) {
            Document ratingDocument = new Document();
            ratingDocument.append("rate", product.getRating().getRate());
            ratingDocument.append("count", product.getRating().getCount());
            document.append("rating", ratingDocument);
        }

        return document;
    }

    public static Product mapDocumentToProduct(Document document) {
        Product product = new Product();
        product.setId(document.getObjectId("_id").toString());
        product.setTitle(document.getString("title"));
        product.setPrice(document.getDouble("price"));
        product.setDescription(document.getString("description"));
        product.setCategory(document.getString("category"));
        product.setImageUrl(document.getString("imageUrl"));
        //product.setCreatedAt(document.get("createdAt", LocalDateTime.class));
        //product.setUpdatedAt(document.get("updatedAt", LocalDateTime.class));

        Document ratingDocument = document.get("rating", Document.class);
        if (ratingDocument != null) {
            Product.Rating rating = new Product.Rating();
            rating.setRate(ratingDocument.getDouble("rate"));
            rating.setCount(ratingDocument.getDouble("count"));
            product.setRating(rating);
        }

        return product;
    }

}