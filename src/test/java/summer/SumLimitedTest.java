package summer;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static summer.Endpoints.SUM_LIMITED;

public class SumLimitedTest extends BaseTest {

    private static int THROUGHPUT = 1;
    private static int SC_TOO_MANY_REQUESTS = 429;

    @Test
    public void shouldReturnSumForOnlySomeRequestByThroughput() {
        //given
        long firstParamValue = RANDOM.nextLong();
        long secondParamValue = RANDOM.nextLong();
        long start = System.currentTimeMillis();
        int seconds = 3;
        List<Response> responses = new ArrayList<>();

        //when I send many responses for some given time

        while (System.currentTimeMillis() < start + seconds * 1000) {
            responses.addAll(
                    Stream.generate(() -> given()
                            .param(FIRST_PARAMETER_NAME, firstParamValue)
                            .param(SECOND_PARAMETER_NAME, secondParamValue)
                            .get(SUM_LIMITED))
                            .limit(20)
                            .collect(Collectors.toList()));
        }

        //then some requests should be failed according to throughput
        Assert.assertThat("There should be many requests sent for this test",
                responses, hasSize(greaterThan(seconds * THROUGHPUT)));

        List<Response> correctResponses = responses.stream()
                .filter(r -> r.getStatusCode() != SC_TOO_MANY_REQUESTS).collect(Collectors.toList());
        List<Response> incorrectResponses = responses.stream()
                .filter(r -> r.getStatusCode() == SC_TOO_MANY_REQUESTS).collect(Collectors.toList());

        Assert.assertThat("Requests rate should be limited to " + THROUGHPUT + "requests per second",
                correctResponses, hasSize(seconds * THROUGHPUT));

        correctResponses
                .forEach(response -> response.then()
                        .assertThat()
                        .statusCode(equalTo(SC_OK))
                        .body(RESULT_PATH, notNullValue())
                        .body(RESULT_PATH, equalTo(firstParamValue + secondParamValue)));

        incorrectResponses
                .forEach(response -> response.then()
                        .assertThat()
                        .statusCode(equalTo(SC_TOO_MANY_REQUESTS))
                        .body(ERROR_PATH, notNullValue())
                        .body(ERROR_PATH, equalTo(TOO_MANY_REQUESTS_ERROR)));
    }

}
