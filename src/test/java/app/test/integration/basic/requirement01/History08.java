package app.test.integration.basic.requirement01;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero asignar un alias a una ubicación, con el fin de personalizar el uso del sistema.
public class History08 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var alias = "CS";
        var name = "Castellón";
        var locationMock = new LocationModel(name, 39.97, -0.05);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.reset(spy.accountManager);

        // When
        var response = client.location.updateLocation(coords, alias);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.location.getLocations();
        state.body("", hasSize(1));
        state.body("alias", hasItem(alias));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellón";
        var locationMock = new LocationModel(name, 39.97, -0.05);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);
        Mockito.reset(spy.accountManager);

        // When
        // No operation

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        var state = client.location.getLocations();
        state.body("", hasSize(1));
        state.body("alias", hasItem(name));
    }
}
