import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SampleTest {

    public static Object[][] za() {
        return new Object[][]{
                {"us", "90210", "Beverly Hills"},
                {"us", "12345", "Schenectady"},
                {"ca", "B2R", "Waverley"}
        };
    }

    private static ResponseSpecification rs;

    @BeforeEach
    public void createResponse() {
        rs = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @ParameterizedTest
    @MethodSource("za")
    public void testMethod(String countryCode, String zipCode, String cityName) {
        given()
                .pathParam("countryCode", countryCode)
                .pathParam("zipCode", zipCode)
                .when()
                .log().uri()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .spec(rs)
                .body("places[0].'place name'",equalTo(cityName));

    }
}
