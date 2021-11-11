package app.acceptance.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;

public class History06 extends TestBase {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";

        // When
        var response = setupRequest("/query/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
    }

    @Test
    public void invalid() {
        // Given
        var name = "ABCDEF";

        // When
        var response = setupRequest("/query/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(0));
    }
}

