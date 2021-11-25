package app.test.integration.basic.requirement02.history04;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.model.LocationModel;
import app.test.generic.SessionTest;

// TODO
// Como usuario quiero consultar fácilmente la información del clima sobre un una ubicación activa
public class Subhistory02 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);

        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.503, -0.405);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);

        name = "Castellon";
        locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.doReturn(true).when(spy.weatherService).getData(locationMock);

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
        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);

        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.503, -0.405);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        name = "Castellon";
        locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        location = client.location.addLocation(name);
        coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);
        Mockito.doReturn(true).when(spy.weatherService).getData(locationMock);

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
