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
https://recipe-api-q3aa.onrender.com/swagger-ui-doc.html

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

API is running on server so in order to access the api you can simply refer to the url:
https://recipe-api-q3aa.onrender.com/recipes
and apply your specified http method and params.

In order to get information about total number of requests refer to the url:
https://recipe-api-q3aa.onrender.com/actuator/metrics/http.server.requests




