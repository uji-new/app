package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero dar de alta una ubicación a partir de unas coordenadas geográficas, con el fin de tenerla disponible en el sistema.
public class History02 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var coords = "39.978,-0.033";

        // When
        var response = client.place.newPlace(coords, name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.place.getPlaces();
        state.body("size()", equalTo(1));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        var coords = "180.0,360.0";

        // When
        var response = client.place.newPlace(coords, name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.place.getPlaces();
        state.body("size()", equalTo(0));
    }
}
