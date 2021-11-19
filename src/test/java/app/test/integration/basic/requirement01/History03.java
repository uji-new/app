package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.BaseTest;

// Como usuario quiero validar el topónimo de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History03 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER.name();
        var location = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(location).when(spy.queryManager).getData(name);
        client.service.newService(type);
        client.place.newPlace(name, name);

        name = "Valencia";
        location = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(location).when(spy.queryManager).getData(name);
        Mockito.doReturn(true).when(spy.weatherService).getData(location);

        // When
        var response = client.service.getServicesForPlace(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
        response.body("get(0).service.type", equalTo(type));
        response.body("get(0).data", equalTo(true));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(name);

        // When
        var response = client.service.getServicesForPlace(name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
