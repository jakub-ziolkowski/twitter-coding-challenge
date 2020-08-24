twitter-coding-challenge
==========================
Spring Boot implementation of recruitment task

##Required tools and libraries
* Git
* Apache Maven
* JRE & JDK 8

##Building application
Open Your favourite terminal and checkout the repository

    git clone https://github.com/jakub-ziolkowski/twitter-coding-challenge.git

Change the working directory to `twitter-coding-challenge`

    cd twitter-coding-challenge

Build everything using Maven

    mvn clean install

To start spring-boot application and run JUnit tests type
    
    mvn spring-boot:run

Built in application server listens on http://localhost:8080

For convenience on http://localhost:8080/swagger-ui.html You can find Swagger UI with all REST API methods documented

##Integration tests PoC
Please use newest version of Postman to run integration tests.
To do that, import Postman collection file from /postman/Simple Social Networking API.postman_collection.json 
and run it with Collection Runner.