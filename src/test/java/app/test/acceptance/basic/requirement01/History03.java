package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.BaseTest;

// Como usuario quiero validar el topónimo de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History03 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER.name();
        serviceClient.newService(type);
        placeClient.newPlace(name, name);
        name = "Valencia";

        // When
        var response = serviceClient.getServicesForPlace(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
        response.body("get(0).service.type", equalTo(type));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";

        // When
        var response = serviceClient.getServicesForPlace(name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
