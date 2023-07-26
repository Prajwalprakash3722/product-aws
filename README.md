# AWS Lambda

## Description

This repo exhibits a simple AWS Lambda function that is developed using Java (runtime 17) and MongoDB with Test Cases.

## Prerequisites

* Java 17
* Maven 3.8.3
* MongoDB 4.4.9 (For local Testing)
* AWS CLI 2.2.46 (For Deployment) (Optional)

## Installation

* Clone the repo
* Run `mvn clean install` to build the project make sure you have local MongoDB running on port 27017 or else the test
  cases will fail, if you don't want to run the
  test cases then run `mvn clean install -DskipTests`
* Run `mvn clean package` to create the jar file, same as above if you don't want to run the test cases then run `mvn
  clean package -DskipTests`

## Adding Data to MongoDB

You can find a class named `DataMigration` in the `com.ecommerce.lambda` package, you can run this class to add data to
the MongoDB. Be sure to set the env variable "MONGODB_CONNECTION_STRING" to the MongoDB URL, if you don't set this
variable then the program will try to connect to the local MongoDB instance.

If you want to add 1000 products set the requestCount to 10 and productsPerRequest to 100, this will send 1000 product

```java
int requestCount=10;
int productsPerRequest=100;
```

The `DataMigration` class is only necessary if you want to add data to the MongoDB database before deploying the Lambda
function.

## How to Deploy in AWS

* Create a Lambda function in AWS with Java 17 as the runtime
* Upload the jar file to the Lambda function
* Set the handler to `com.ecommerce.App::handleRequest`
* Set the env variable "MONGODB_CONNECTION_STRING" to the MongoDB URL
* Generate Function URL without any authentication (IAM NONE) and use it to test the API