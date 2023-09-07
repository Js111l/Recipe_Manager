### Recipe_Manager project

The API provides basic functionality of managing recipes.

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info

Recipe API provides flexible functionality related of managing recipes.
Each recipe contains information about ingredients, preparation time
and steps which guides you how to prepare the food.
With the API you can load existing recipes and add your own. Also you can
delete and update existing ones.

#### Paths

Copy and paste ```openapi.yml``` file from ```java/resources``` to https://editor-next.swagger.io/
in order to view the swagger documentation for this api.
Alternatively, after you successfully launched the project, you can access the path
/swagger-ui-doc.html (
e.g. http://localhost:8080/swagger-ui-doc.html) and see full swagger documentation.

## Technologies

* Java 17
* Spring Boot 3.1.0
* Spring Data JPA (Hibernate) 3.1.0
* Docker 24.0.2
* Postgres 15

##### Libraries:

* Lombok

##### Testing:

* WebTestClient
* TestContainers

## Setup

In order to set up the project, you would need to have established connection to the database.
Firstly, make sure you have Docker installed on your machine. Then
issue the ```docker-compose -f docker_compose.yml up``` command in this directory to set up proper
environment.




