package summer;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.lang.Integer.toHexString;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static summer.Endpoints.SUM_LAZY;

/*
TASK: Write tests for `/sumLazy` endpoint. It will return response to your request in up to 2 seconds. If serving
request takes longer then that, the test should fail.
 */
public class SumLazyTest extends BaseTest {

    private static final long LAZY_TIMEOUT_MILLISECONDS = 2000;

    @Test
    public void withCorrectParametersShouldReturnSumInTwoSeconds() {

        long firstParam = RANDOM.nextLong();
        long secondParam = RANDOM.nextLong();

        given()
                .param(FIRST_PARAMETER_NAME, firstParam)
                .param(SECOND_PARAMETER_NAME, secondParam)
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(RESULT_PATH, notNullValue())
                .body(RESULT_PATH, equalTo(firstParam + secondParam))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));

    }

    @Test
    public void withCorrectParametersReverseOrderShouldReturnSumInTwoSeconds() {
        long firstParam = RANDOM.nextLong();
        long secondParam = RANDOM.nextLong();

        given()
                .param(SECOND_PARAMETER_NAME, secondParam)
                .param(FIRST_PARAMETER_NAME, firstParam)
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(RESULT_PATH, notNullValue())
                .body(RESULT_PATH, equalTo(firstParam + secondParam))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    //is it a correct behaviour?
    @Test
    public void withMaxLongParametersShouldReturnOverflowSumInTwoSeconds() {
        long firstParam = Long.MAX_VALUE;
        long secondParam = 1;
        long result = Long.MIN_VALUE;

        given()
                .param(SECOND_PARAMETER_NAME, secondParam)
                .param(FIRST_PARAMETER_NAME, firstParam)
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_OK))
                .body(RESULT_PATH, notNullValue())
                .body(RESULT_PATH, equalTo(result))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void withIncorrectParametersShouldReturnExpectedNumericParamErrorInTwoSeconds() {
        int intParam = RANDOM.nextInt();
        String stringParam = toHexString(RANDOM.nextInt());

        given()
                .param(FIRST_PARAMETER_NAME, intParam)
                .param(SECOND_PARAMETER_NAME, stringParam)
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(EXPECTED_NUMERIC_PARAM_ERROR))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void postShouldReturnMethodNotAllowedResponseCodeInTwoSeconds() {
        given()
                .when()
                .post(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void putShouldReturnMethodNotAllowedResponseCodeInTwoSeconds() {
        given()
                .when()
                .put(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void deleteShouldReturnMethodNotAllowedResponseCodeInTwoSeconds() {
        given()
                .when()
                .delete(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void headShouldReturnMethodNotAllowedResponseCodeInTwoSeconds() {
        given()
                .when()
                .head(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void optionsShouldReturnMethodNotAllowedResponseCodeInTwoSeconds() {
        given()
                .when()
                .options(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void patchShouldReturnMethodNotAllowedResponseCodeInTwoSeconds() {
        given()
                .when()
                .patch(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_METHOD_NOT_ALLOWED))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void withoutAuthenticationShouldReturnUnauthorizedErrorInTwoSeconds() {
        given()
                .param(FIRST_PARAMETER_NAME, RANDOM.nextInt())
                .param(SECOND_PARAMETER_NAME, RANDOM.nextInt())
                .auth().none()
                .header("Authorization", "Bearer " + toHexString(RANDOM.nextInt()))
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_UNAUTHORIZED))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(UNAUTHORIZED_ERROR))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void withoutParametersShouldReturnMissingParameterErrorInTwoSeconds() {
        given()
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(MISSING_PARAM_ERROR))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }

    @Test
    public void withoutOneParameterShouldReturnMissingParameterErrorInTwoSeconds() {
        given()
                .param(FIRST_PARAMETER_NAME, RANDOM.nextInt())
                .when()
                .get(SUM_LAZY)
                .then()
                .assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST))
                .body(ERROR_PATH, notNullValue())
                .body(ERROR_PATH, equalTo(MISSING_PARAM_ERROR))
                .time(lessThan(LAZY_TIMEOUT_MILLISECONDS));
    }
}
