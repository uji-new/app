package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero validar el topónimo de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History03 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER.name();
        var locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);
        client.service.enableService(type);

        name = "Valencia";
        locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        Mockito.doReturn(true).when(spy.weatherService).getData(locationMock);

        // When
        var response = client.service.getServicesForLocation(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("findAll{it.active}.size()", equalTo(1));
        response.body("findAll{it.active}.service.type", hasItem(type));
        response.body("findAll{it.active}.data", hasItem(true));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(name);

        // When
        var response = client.service.getServicesForLocation(name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
