package app.acceptance.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;

public class History02 extends TestBase {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var coords = "39.978,-0.033";

        // When
        var response = setupRequest("/places/{query}").pathParam("query", coords).queryParam("alias", name).post().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = setupRequest("/places").get().then();
        state.body("size()", equalTo(1));
    }

    @Test
    public void invalid() {
        // Given
        var name = "ABCDEF";
        var coords = "180.0,360.0";

        // When
        var response = setupRequest("/places/{query}").pathParam("query", coords).queryParam("alias", name).post().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = setupRequest("/places").get().then();
        state.body("size()", equalTo(0));
    }
}
