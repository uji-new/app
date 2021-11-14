package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero obtener las coordenadas geográficas de una ubicación a partir de su topónimo, con el fin de facilitar la obtención de información en múltiples fuentes públicas (API).
public class History06 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";

        // When
        var response = queryClient.query(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";

        // When
        var response = queryClient.query(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(0));
    }
}

