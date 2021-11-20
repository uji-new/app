package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.BaseTest;

// Como usuario quiero activar una ubicación disponible en el sistema, con el fin de recibir información relacionada con dicha ubicación.
public class History05 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var location = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(location).when(spy.queryManager).getData(name);
        client.place.addLocation(name, name);
        var coords = client.place.getLocations().extract().jsonPath().getString("get(0).coords");
        client.place.removeLocation(coords);

        // When
        var response = client.history.restoreLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = client.place.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("size()", equalTo(1));
        statePlaces.body("get(0).name", equalTo(name));
        stateHistory.body("size()", equalTo(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        var location = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(location).when(spy.queryManager).getData(name);
        client.place.addLocation(name, name);
        var coords = client.place.getLocations().extract().jsonPath().getString("get(0).coords");

        // When
        var response = client.history.restoreLocation(coords);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = client.place.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("size()", equalTo(1));
        statePlaces.body("get(0).name", equalTo(name));
        stateHistory.body("size()", equalTo(0));
    }
}