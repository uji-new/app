package app.test.acceptance.basic.requirement01;


import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero activar una ubicación disponible en el sistema, con el fin de recibir información relacionada con dicha ubicación.
public class History05 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        placeClient.newPlace(name, name);
        var coords = placeClient.getPlaces().extract().jsonPath().getString("get(0).coords");
        placeClient.deletePlace(coords);

        // When
        var response = historyClient.newPlace(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = placeClient.getPlaces();
        var stateHistory = historyClient.getPlaces();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        placeClient.newPlace(name, name);
        var coords = placeClient.getPlaces().extract().jsonPath().getString("get(0).coords");

        // When
        var response = historyClient.newPlace(coords);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = placeClient.getPlaces();
        var stateHistory = historyClient.getPlaces();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }
}
