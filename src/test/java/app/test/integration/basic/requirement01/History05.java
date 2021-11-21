package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero activar una ubicación disponible en el sistema, con el fin de recibir información relacionada con dicha ubicación.
public class History05 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name, name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.history.restoreLocation(coords);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = client.location.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("size()", equalTo(1));
        statePlaces.body("get(0).name", equalTo(name));
        stateHistory.body("size()", equalTo(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        var locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name, name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.reset(spy.accountManager);

        // When
        var response = client.history.restoreLocation(coords);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = client.location.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("size()", equalTo(1));
        statePlaces.body("get(0).name", equalTo(name));
        stateHistory.body("size()", equalTo(0));
    }
}