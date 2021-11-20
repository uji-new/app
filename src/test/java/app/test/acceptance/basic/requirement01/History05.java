package app.test.acceptance.basic.requirement01;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero activar una ubicación disponible en el sistema, con el fin de recibir información relacionada con dicha ubicación.
public class History05 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var location = client.location.addLocation(name, name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.history.restoreLocation(coords);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(Mockito.any());
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = client.location.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        var location = client.location.addLocation(name, name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.reset(spy.accountManager);

        // When
        var response = client.history.restoreLocation(coords);

        // Then
        Mockito.verify(spy.accountManager, Mockito.never()).saveAccount(Mockito.any());
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = client.location.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }
}
