package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static final String KEY = "YOUR KEY";
    protected static final String TOKEN = "YOUR TOKEN";
    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String BOARDS = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARDS = "cards";
    protected static final String ORGANIZATIONS = "organizations";

    protected static RequestSpecification reqSpecification;
    protected static Faker faker;

    @BeforeAll
    public static void beforeAll(){

        reqSpecification = new RequestSpecBuilder()
                .addQueryParam("key", KEY)
                .addQueryParam("token", TOKEN)
                .setContentType(ContentType.JSON)
                .build();

        faker = new Faker();
    }
}
