package app.acceptance.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;

public class History01 extends TestBase {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";

        // When
        var response = setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = setupRequest("/places").get().then();
        state.body("size()", equalTo(1));
    }

    @Test
    public void invalid() {
        // Given
        var name = "ABCDEF";

        // When
        var response = setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = setupRequest("/places").get().then();
        state.body("size()", equalTo(0));
    }
}
