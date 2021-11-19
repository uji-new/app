package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.BaseTest;

// Como usuario quiero validar las coordenadas geográficas de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History04 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Valencia";
        var type = ServiceType.WEATHER.name();
        var location = new LocationModel(name, 39.980, -0.033);
        var coords = location.getCoords();
        Mockito.doReturn(location).when(spy.queryManager).getData(coords);
        Mockito.doReturn(true).when(spy.weatherService).getData(location);
        client.service.newService(type);
        client.place.newPlace(coords, name);

        // When
        var response = client.service.getServicesForPlace(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
        response.body("get(0).service.type", equalTo(type));
    }

    @Test
    public void invalid() {
        // Given
        var coords = "180,360";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(coords);

        // When
        var response = client.service.getServicesForPlace(coords);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
