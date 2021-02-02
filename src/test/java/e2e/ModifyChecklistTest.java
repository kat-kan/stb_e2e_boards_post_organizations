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
public class ModifyChecklistTest extends BaseTest {

    private String boardId;
    private String listId;
    private String cardId;
    private String checklistId;
    private String firstCheckItemId;
    private String secondCheckItemId;


    @Test
    @Order(1)
    public void createNewBoard(){

        String e2eBoardName = "E2E checklist board";

        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", e2eBoardName)
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        boardId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo(e2eBoardName);
    }

    @Test
    @Order(2)
    public void createList(){

        String e2eListName = "E2E checklist list";

        Response response = given()
                .spec(reqSpecification)
                .queryParam("idBoard", boardId)
                .queryParam("name", e2eListName)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        listId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo(e2eListName);

    }


    @Test
    @Order(3)
    public void addCardToList(){

        String e2eCardName = "E2E checklist card";

        Response response = given()
                .spec(reqSpecification)
                .queryParam("idList", listId)
                .queryParam("name", e2eCardName)
                .when()
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        cardId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo(e2eCardName);
    }

    @Test
    @Order(4)
    public void createChecklist(){

        String e2eChecklistName = "E2E checklist name";

        Response response = given()
                .spec(reqSpecification)
                .queryParam("idCard", cardId)
                .queryParam("name", e2eChecklistName)
                .when()
                .post(BASE_URL + CHECKLISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        checklistId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo(e2eChecklistName);

    }

    @Test
    @Order(5)
    public void createFirstCheckItem(){

        String e2eCheckItemName = "E2E first checkItem name";

        Response response = given()
                .spec(reqSpecification)
                .pathParam("id", checklistId)
                .queryParam("name", e2eCheckItemName)
                .when()
                .post(BASE_URL + CHECKLISTS + "/{id}/" + CHECKITEMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        firstCheckItemId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo(e2eCheckItemName);

    }

    @Test
    @Order(6)
    public void createSecondCheckItem(){

        String e2eCheckItemName = "E2E second checkItem name";

        Response response = given()
                .spec(reqSpecification)
                .pathParam("id", checklistId)
                .queryParam("name", e2eCheckItemName)
                .when()
                .post(BASE_URL + CHECKLISTS + "/{id}/" + CHECKITEMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        secondCheckItemId = json.getString("id");
        Assertions.assertThat(json.getString("name")).isEqualTo(e2eCheckItemName);

    }

    @Test
    @Order(7)
    public void completeFirstCheckItem(){

        String completedState = "complete";

        Response response = given()
                .spec(reqSpecification)
                .queryParam("state", completedState)
                .pathParam("idCard", cardId)
                .pathParam("idCheckItem", firstCheckItemId)
                .pathParam("idChecklist", checklistId)
                .when()
                .put(BASE_URL + CARDS + "/{idCard}/" + "checklist" + "/{idChecklist}/" + "checkItem" + "/{idCheckItem}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("state")).isEqualTo(completedState);
    }

    @Test
    @Order(8)
    public void deleteSecondCheckItem(){

        given()
                .spec(reqSpecification)
                .pathParam("id", checklistId)
                .pathParam("idCheckItem", secondCheckItemId)
                .when()
                .delete(BASE_URL + CHECKLISTS + "/{id}/" + CHECKITEMS + "/{idCheckItem}")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }


    @Test
    @Order(9)
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
