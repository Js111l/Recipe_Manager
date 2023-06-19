package com.softwareminds.recipemanager.controller;

import com.softwareminds.recipemanager.database.DbInit;
import com.softwareminds.recipemanager.models.Ingredient;
import com.softwareminds.recipemanager.models.Recipe;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecipeControllerTest {
  @Container
  public static MySQLContainer mySQLContainer =
      new MySQLContainer<>("mysql:latest")
          .withDatabaseName("test")
          .withUsername("root")
          .withPassword("qwerty");

  static final String RECIPES_URL = "recipes";
  List<Recipe> RECIPE_LIST;
  @Autowired WebTestClient webTestClient;
  @Autowired DbInit dbInit;

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @BeforeEach
  void setUp() {
    mySQLContainer.start();
    dbInit.initTestData();
    this.RECIPE_LIST = dbInit.getRecipesList();
  }

  @AfterAll
  static void tearDown() {
    mySQLContainer.stop();
  }

  @Test
  void getAllRecipes_void_properListOfEntities() {
    var result =
        this.webTestClient
            .get()
            .uri(RECIPES_URL)
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result).hasSameElementsAs(RECIPE_LIST);
  }

  @Test
  void getAllRecipesWithPagination_firstPage_properListOfRecipes() {

    var result =
        this.webTestClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path(RECIPES_URL)
                        .queryParam("page", 0)
                        .queryParam("size", 3)
                        .queryParam("direction", "ASC")
                        .build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result).hasSameElementsAs(RECIPE_LIST.subList(0, 3));
  }

  @Test
  void getAllRecipesWithPagination_firstPageSize6_properListWithProperSize() {

    var result =
        this.webTestClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path(RECIPES_URL)
                        .queryParam("page", 0)
                        .queryParam("size", 6)
                        .queryParam("direction", "ASC")
                        .build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result).hasSameElementsAs(RECIPE_LIST.subList(0, 6));
  }

  @Test
  void getAllRecipesWithPagination_firstPageSize0_badRequest() {

    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(RECIPES_URL)
                    .queryParam("page", 0)
                    .queryParam("size", 0)
                    .queryParam("direction", "ASC")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  void getRecipeById_validId_properEntity() {
    var result =
        this.webTestClient
            .get()
            .uri(RECIPES_URL + "/5")
            .exchange()
            .expectBody(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result)
        .isEqualTo(RECIPE_LIST.stream().filter(x -> x.id() == 5).findFirst().orElseThrow());
  }

  @Test
  void getRecipeById_inValidId_statusNotFound() {
    var response =
        this.webTestClient
            .get()
            .uri(RECIPES_URL + "/999")
            .exchange()
            .expectBody(ProblemDetail.class)
            .returnResult()
            .getResponseBody();

    assertThat(response.getTitle()).isEqualTo("RECIPE_NOT_FOUND");
  }

  @Test
  void getRecipesFilterByName_validName_properEntity() {
    var result =
        this.webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(RECIPES_URL).queryParam("name", "Recipe5").build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result)
        .hasSameElementsAs(RECIPE_LIST.stream().filter(x -> x.name().equals("Recipe5")).toList());
  }

  @Test
  void getRecipesFilterByName_validNameUpperCase_properEntity() {
    var result =
        this.webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(RECIPES_URL).queryParam("name", "RECIPE5").build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result)
        .hasSameElementsAs(RECIPE_LIST.stream().filter(x -> x.name().equals("Recipe5")).toList());
  }

  @Test
  void getRecipesFilterByName_validNamePrefix_properEntities() {
    var result =
        this.webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(RECIPES_URL).queryParam("name", "Reci").build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result).hasSameElementsAs(RECIPE_LIST);
  }

  @Test
  void getRecipesFilterByName_validNameLowerCase_properEntity() {
    var result =
        this.webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(RECIPES_URL).queryParam("name", "recipe5").build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result)
        .hasSameElementsAs(RECIPE_LIST.stream().filter(x -> x.name().equals("Recipe5")).toList());
  }

  @Test
  void getRecipesFilterByName_inValidName_emptyList() {
    var result =
        this.webTestClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder.path(RECIPES_URL).queryParam("name", "InvalidName").build())
            .exchange()
            .expectBodyList(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result).isEqualTo(Collections.EMPTY_LIST);
  }

  @Test
  void getRecipesIngredients_validId_properListOfIngredients() {
    var result =
        this.webTestClient
            .get()
            .uri(RECIPES_URL + "/5/ingredients")
            .exchange()
            .expectBodyList(Ingredient.class)
            .returnResult()
            .getResponseBody();

    assertThat(result)
        .hasSameElementsAs(
            RECIPE_LIST.stream()
                .filter(x -> x.id() == 5)
                .flatMap(x -> x.ingredients().stream())
                .toList());
  }

  @Test
  void getRecipesIngredients_inValidId_statusNotFound() {
    var response =
        this.webTestClient
            .get()
            .uri(RECIPES_URL + "/211/ingredients")
            .exchange()
            .expectStatus()
            .isNotFound()
            .expectBody(ProblemDetail.class)
            .returnResult()
            .getResponseBody();

    assertThat(response.getTitle()).isEqualTo("RECIPE_NOT_FOUND");
  }

  @Test
  void putRecipe_properRecipeObject_properlyUpdatedResource() {
    var recipe =
        new Recipe(
            5,
            "Updated Recipe!",
            Duration.of(2, ChronoUnit.HOURS).toString(),
            "Short description of a recipe",
            Collections.EMPTY_LIST,
            Collections.EMPTY_LIST);

    var result =
        this.webTestClient
            .put()
            .uri(RECIPES_URL)
            .bodyValue(recipe)
            .exchange()
            .expectBody()
            .jsonPath("$.id")
            .value(equalTo(5))
            .jsonPath("$.name")
            .value(equalTo(recipe.name()))
            .jsonPath("$.description")
            .value(equalTo(recipe.description()))
            .jsonPath("$.prepTime")
            .value(equalTo(recipe.prepTime()));
  }

  @Test
  void putRecipe_recipeWithInvalidId_statusNotFound() {
    var recipe =
        new Recipe(
            999,
            "Updated Recipe!",
            Duration.of(2, ChronoUnit.HOURS).toString(),
            "Short description of a recipe",
            Collections.EMPTY_LIST,
            Collections.EMPTY_LIST);

    var response =
        this.webTestClient
            .put()
            .uri(RECIPES_URL)
            .bodyValue(recipe)
            .exchange()
            .expectStatus()
            .isNotFound()
            .expectBody(ProblemDetail.class)
            .returnResult()
            .getResponseBody();

    assertThat(response.getTitle()).isEqualTo("RECIPE_NOT_FOUND");
  }

  @Test
  void postRecipe() {
    var recipe =
        new Recipe(
            7,
            "Another Recipe!",
            Duration.of(8, ChronoUnit.HOURS).toString(),
            "Short description of a recipe",
            Collections.EMPTY_LIST,
            Collections.EMPTY_LIST);

    this.webTestClient.post().uri(RECIPES_URL).bodyValue(recipe).exchange().expectStatus().isOk();

    var result =
        this.webTestClient
            .get()
            .uri(RECIPES_URL + "/7")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Recipe.class)
            .returnResult()
            .getResponseBody();

    assertThat(result).isEqualTo(recipe);
  }

  @Test
  void deleteRecipe_validId_properlyDeletedResource() {
    this.webTestClient.delete().uri(RECIPES_URL + "/5").exchange().expectStatus().isOk();
    this.webTestClient.get().uri(RECIPES_URL + "/5").exchange().expectStatus().isNotFound();
  }

  @Test
  void deleteRecipe_inValidId_statusNotFound() {
    var response =
        this.webTestClient
            .delete()
            .uri(RECIPES_URL + "/999")
            .exchange()
            .expectStatus()
            .isNotFound()
            .expectBody(ProblemDetail.class)
            .returnResult()
            .getResponseBody();

    assertThat(response.getTitle()).isEqualTo("RECIPE_NOT_FOUND");
  }
}
