package app.integration.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.*;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import app.error.MissingError;
import app.generic.TestBase;
import app.manager.QueryManager;
import app.model.LocationModel;
import app.rest.service.WeatherService;
import app.rest.service.generic.ServiceType;

public class History03 extends TestBase {
    @SpyBean QueryManager queries;
    @SpyBean WeatherService weather;

    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER;
        var location = new LocationModel();
        location.setName(name);
        location.setLatitude(39.980);
        location.setLongitude(-0.033);
        Mockito.doReturn(location).when(queries).getData(name);
        setupRequest("/services").queryParam("type", type).post();
        setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post();

        name = "Valencia";
        location = new LocationModel();
        location.setName(name);
        location.setLatitude(39.980);
        location.setLongitude(-0.033);
        Mockito.doReturn(location).when(queries).getData(name);
        Mockito.doReturn(true).when(weather).getData(location);

        // When
        var response = setupRequest("/services/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.log().body();
        response.body("size()", equalTo(1));
        response.body("get(0).service.type", equalTo(type.name()));
        response.body("get(0).data", equalTo(true));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doThrow(new MissingError()).when(queries).getData(name);

        // When
        var response = setupRequest("/services/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
