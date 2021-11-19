package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.BaseTest;

// Como usuario quiero dar de alta una ubicación a partir de un topónimo, con el fin de tenerla disponible en el sistema.
public class History01 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var location = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(location).when(spy.queryManager).getData(name);

        // When
        var response = client.place.newPlace(name, name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.place.getPlaces();
        state.body("size()", equalTo(1));
        state.body("get(0).name", equalTo(name));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(name);

        // When
        var response = client.place.newPlace(name, name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.place.getPlaces();
        state.body("size()", equalTo(0));
    }
}
