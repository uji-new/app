package app.test.integration.basic.requirement01;

import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero obtener el topónimo más próximo a las coordenadas geográficas de una ubicación, con el fin de facilitar la obtención de información en múltiples fuentes públicas (API).
public class History07 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var location = new LocationModel(name, 39.980, -0.033);
        var coords = location.getCoords();
        Mockito.doReturn(List.of(location)).when(spy.queryManager).getAllData(coords);

        // When
        var response = client.query.query(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
        response.body("get(0).name", equalTo(name));
    }

    @Test
    public void invalid() {
        // Given
        var coords = "180,360";
        Mockito.doReturn(Collections.emptyList()).when(spy.queryManager).getAllData(coords);

        // When
        var response = client.query.query(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(0));
    }
}