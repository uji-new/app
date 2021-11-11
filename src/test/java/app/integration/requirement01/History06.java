package app.integration.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;
import app.manager.QueryManager;
import app.model.LocationModel;

import java.util.Collections;
import java.util.List;

public class History06 extends TestBase {
    @SpyBean QueryManager queries;

    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var location = new LocationModel();
        location.setName(name);
        location.setLatitude(39.980);
        location.setLongitude(-0.033);
        var coords = location.getCoords();
        Mockito.doReturn(List.of(location)).when(queries).getAllData(name);

        // When
        var response = setupRequest("/query/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(1));
        response.body("get(0).coords", equalTo(coords));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doReturn(Collections.emptyList()).when(queries).getAllData(name);

        // When
        var response = setupRequest("/query/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(0));
    }
}