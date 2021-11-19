package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero dar de alta una ubicación a partir de un topónimo, con el fin de tenerla disponible en el sistema.
public class History01 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";

        // When
        var response = client.place.newPlace(name, name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.place.getPlaces();
        state.body("size()", equalTo(1));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";

        // When
        var response = client.place.newPlace(name, name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.place.getPlaces();
        state.body("size()", equalTo(0));
    }
}
