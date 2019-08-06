package summer;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import org.junit.BeforeClass;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static summer.Endpoints.API_URL;
import static summer.Endpoints.AUTH;

public abstract class BaseTest {
    public static final Random RANDOM = new Random();
    public static final String ERROR_PATH = "error";
    public static final String TOKEN_PATH = "token";
    public static final String UNAUTHORIZED_ERROR = "Unauthorized";
    public static final String MISSING_PARAM_ERROR = "Missing Param";
    public static final String EXPECTED_NUMERIC_PARAM_ERROR = "Expected Numeric Param";


    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        RestAssured.baseURI = API_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        Map<String, Object> credentials = new Yaml().load(new FileInputStream("credentials.yml"));
        String credentialsBody = credentials.entrySet().stream()
                .map(e -> String.format("\"%s\":\"%s\"", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));

        String token = given()
                .body(credentialsBody)
                .when()
                .post(AUTH)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(TOKEN_PATH, notNullValue())
                .extract().path(TOKEN_PATH);

        RestAssured.authentication = new PreemptiveBasicAuthScheme() {
            @Override
            public String generateAuthToken() {
                return "Bearer " + token;
            }
        };

    }

}