package com.recipemanager.controller;

import com.recipemanager.database.DbInit;
import com.recipemanager.models.Ingredient;
import com.recipemanager.models.Recipe;
import com.recipemanager.models.Step;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
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
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
class RecipeControllerTest {

  private static final List<Ingredient> EMPTY_INGREDIENTS_LIST = new ArrayList<>();
  private static final List<Step> EMPTY_STEP_LIST = new ArrayList<>();

  @Container
  @SuppressWarnings({"raw", "resource"})
  public static PostgreSQLContainer postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("test")
          .withUsername("root")
          .withPassword("qwerty");

  private static final String RECIPES_URL = "recipes";
  private List<Recipe> RECIPE_LIST;
  @Autowired private WebTestClient webTestClient;
  @Autowired private DbInit dbInit;

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    postgreSQLContainer.start();
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @BeforeEach
  void setUp() {
    dbInit.initTestData();
    this.RECIPE_LIST = dbInit.getRecipesList();
  }

  @AfterAll
  static void tearDown() {
    postgreSQLContainer.stop();
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
            EMPTY_INGREDIENTS_LIST,
            EMPTY_STEP_LIST);

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
            EMPTY_INGREDIENTS_LIST,
            EMPTY_STEP_LIST);

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
            EMPTY_INGREDIENTS_LIST,
            EMPTY_STEP_LIST);

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
