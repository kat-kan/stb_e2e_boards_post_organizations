package e2e;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoveCardBetweenListsTest extends BaseTest {

    private String boardId;
    private String firstListId;
    private String secondListId;
    private String cardId;


    @Test
    @Order(1)
    public void createNewBoard(){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "E2E board")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        boardId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo("E2E board");
    }

    @Test
    @Order(2)
    public void createFirstList(){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("idBoard", boardId)
                .queryParam("name", "First E2E list")
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        firstListId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo("First E2E list");

    }

    @Test
    @Order(3)
    public void createSecondList(){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("idBoard", boardId)
                .queryParam("name", "Second E2E list")
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        secondListId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo("Second E2E list");

    }

    @Test
    @Order(4)
    public void addCardToFirstList(){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("idList", firstListId)
                .queryParam("name", "E2E card name")
                .when()
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        cardId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo("E2E card name");
    }

    @Test
    @Order(5)
    public void moveCardBetweenLists(){

        Response response = given()
                .spec(reqSpecification)
                .pathParam("id", cardId)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + CARDS + "/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("idList")).isEqualTo(secondListId);
    }

    @Test
    @Order(6)
    public void deleteBoard(){

        given()
                .spec(reqSpecification)
                .pathParam("id", boardId)
                .when()
                .delete(BASE_URL + BOARDS + "/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }
}
