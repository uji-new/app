package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero dar de alta una ubicación a partir de un topónimo, con el fin de tenerla disponible en el sistema.
public class History01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon de la Plana";

        // When
        var response = client.location.addLocation(name, name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.log().body();
        var state = client.location.getLocations();
        state.body("size()", equalTo(1));
        state.body("get(0).name", equalTo(name));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";

        // When
        var response = client.location.addLocation(name, name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.location.getLocations();
        state.body("size()", equalTo(0));
    }
}
