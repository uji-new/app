package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.BaseTest;

// Como usuario quiero dar de alta una ubicación a partir de unas coordenadas geográficas, con el fin de tenerla disponible en el sistema.
public class History02 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var location = new LocationModel(name, 39.980, -0.033);
        var coords = location.getCoords();
        Mockito.doReturn(location).when(spy.queryManager).getData(coords);

        // When
        var response = client.place.addLocation(coords, name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.place.getLocations();
        state.body("size()", equalTo(1));
        state.body("get(0).coords", equalTo(coords));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        var coords = "180.0,360.0";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(coords);

        // When
        var response = client.place.addLocation(coords, name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.place.getLocations();
        state.body("size()", equalTo(0));
    }
}
