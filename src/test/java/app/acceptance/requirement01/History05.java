package app.acceptance.requirement01;


import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;

public class History05 extends TestBase {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post();
        var coords = setupRequest("/places").get().then().extract().jsonPath().getString("get(0).coords");
        setupRequest("/places/{coords}").pathParam("coords", coords).delete();

        // When
        var response = setupRequest("/history/{coords}").pathParam("coords", coords).post().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = setupRequest("/places").get().then();
        var stateHistory = setupRequest("/history").get().then();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post();
        var coords = setupRequest("/places").get().then().extract().jsonPath().getString("get(0).coords");

        // When
        var response = setupRequest("/history/{coords}").pathParam("coords", coords).post().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = setupRequest("/places").get().then();
        var stateHistory = setupRequest("/history").get().then();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }
}
