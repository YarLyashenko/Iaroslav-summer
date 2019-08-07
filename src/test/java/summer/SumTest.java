package summer;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.lang.Integer.toHexString;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static summer.Endpoints.SUM;

/*
TASK: Implement tests of /sum endpoint, testing basic functionality of summing two numbers.
 */
public class SumTest extends BaseTest {

    @Test
    public void withCorrectParametersShouldReturnSum() {
        long firstParamValue = RANDOM.nextLong();
        long secondParamValue = RANDOM.nextLong();

        given()
                .param(FIRST_PARAMETER_NAME, firstParamValue)
                .param(SECOND_PARAMETER_NAME, secondParamValue)
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(RESULT_PATH, notNullValue())
                .body(RESULT_PATH, equalTo(firstParamValue + secondParamValue));
    }

    @Test
    public void withCorrectParametersReverseOrderShouldReturnSum() {
        long firstParamValue = RANDOM.nextLong();
        long secondParamValue = RANDOM.nextLong();

        given()
                .param(SECOND_PARAMETER_NAME, secondParamValue)
                .param(FIRST_PARAMETER_NAME, firstParamValue)
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(RESULT_PATH, notNullValue())
                .body(RESULT_PATH, equalTo(firstParamValue + secondParamValue));
    }

    //is it a correct behaviour?
    @Test
    public void withMaxLongParametersShouldReturnOverflowSum() {
        long firstParamValue = Long.MAX_VALUE;
        long secondParamValue = 1;
        long result = Long.MIN_VALUE;

        given()
                .param(SECOND_PARAMETER_NAME, secondParamValue)
                .param(FIRST_PARAMETER_NAME, firstParamValue)
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(RESULT_PATH, notNullValue())
                .body(RESULT_PATH, equalTo(result));
    }

    @Test
    public void withIncorrectParametersShouldReturnExpectedNumericParamError() {
        int intParam = RANDOM.nextInt();
        String stringParam = toHexString(RANDOM.nextInt());

        given()
                .param(FIRST_PARAMETER_NAME, intParam)
                .param(SECOND_PARAMETER_NAME, stringParam)
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(EXPECTED_NUMERIC_PARAM_ERROR));
    }

    @Test
    public void postShouldReturnMethodNotAllowedResponseCode() {
        given()
                .when()
                .post(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void putShouldReturnMethodNotAllowedResponseCode() {
        given()
                .when()
                .put(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void deleteShouldReturnMethodNotAllowedResponseCode() {
        given()
                .when()
                .delete(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void headShouldReturnMethodNotAllowedResponseCode() {
        given()
                .when()
                .head(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void optionsShouldReturnMethodNotAllowedResponseCode() {
        given()
                .when()
                .options(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void patchShouldReturnMethodNotAllowedResponseCode() {
        given()
                .when()
                .patch(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void withoutAuthenticationShouldReturnUnauthorizedError() {
        given()
                .param(FIRST_PARAMETER_NAME, RANDOM.nextInt())
                .param(SECOND_PARAMETER_NAME, RANDOM.nextInt())
                .auth().none()
                .header("Authorization", "Bearer " + toHexString(RANDOM.nextInt()))
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_UNAUTHORIZED))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(UNAUTHORIZED_ERROR));
    }

    @Test
    public void withoutParametersShouldReturnMissingParameterError() {
        given()
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(MISSING_PARAM_ERROR));
    }

    @Test
    public void withoutOneParameterShouldReturnMissingParameterError() {
        given()
                .param(FIRST_PARAMETER_NAME, RANDOM.nextInt())
                .when()
                .get(SUM)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(MISSING_PARAM_ERROR));
    }
}
