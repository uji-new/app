package app.test.integration.basic.requirement02.history04;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasSize;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información de eventos sobre un una ubicación activa.
public class Subhistory03 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.EVENTS.name();
        client.service.enableService(type);

        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.98, -0.03);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);

        name = "Castellón";
        locationMock = new LocationModel(name, 39.97, -0.05);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.doReturn(true).when(spy.eventsService).getData(locationMock);

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data"), equalTo(true));
    }

    @Test
    public void invalid() {
        // Given
        var type = ServiceType.EVENTS.name();
        client.service.enableService(type);

        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.98, -0.03);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);

        var alias = "Antarctica";
        locationMock = new LocationModel(name, -78.16, 16.41);
        var coords = locationMock.getCoords();
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(coords);
        var location = client.location.addLocation(coords, alias);
        coords = location.extract().jsonPath().getString("coords");
        Mockito.doReturn(Collections.emptyList()).when(spy.eventsService).getData(locationMock);

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data"), hasSize(0));
    }
}
