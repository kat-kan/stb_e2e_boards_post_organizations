package organizations;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

public class CreateNewOrganizationTest extends BaseTest {


    private String description;
    private String name;
    private String website;
    private String orgId;
    private JsonPath json;
    private String orgDisplayName = "Coolest organization";

    @AfterEach
    public void afterEach(){

        orgId = json.getString("id");
        
        if(orgId!=null){
            given()
                    .spec(reqSpecification)
                    .pathParam("id", orgId)
                    .when()
                    .delete(BASE_URL + ORGANIZATIONS + "/{id}")
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }
    }

    @Test
    public void createNewOrganization(){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", orgDisplayName)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        Assertions.assertThat(json.getString("displayName")).isEqualTo(orgDisplayName);

    }

    @Test
    public void createNewOrganizationWithEmptyDisplayName(){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

        String errorMessage = "Display Name must be at least 1 character";
        json = response.jsonPath();
        Assertions.assertThat(json.getString("message")).isEqualTo(errorMessage);

    }

    @Test
    public void createNewOrganizationWithDescription(){

        description = faker.hitchhikersGuideToTheGalaxy().quote();

        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", orgDisplayName)
                .queryParam("desc", description)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        Assertions.assertThat(json.getString("desc")).isEqualTo(description);

    }

    @Test
    public void createNewOrganizationWithName(){

        //generating unique name
        name = faker.lorem().word() + + faker.number().randomNumber() + faker.lorem().word();

        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", orgDisplayName)
                .queryParam("name", name)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo(name);

    }

    @Test
    public void createNewOrganizationWithWebsite(){

        website = faker.internet().url();

        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", orgDisplayName)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        Assertions.assertThat(json.getString("website")).contains(website);

    }

}
