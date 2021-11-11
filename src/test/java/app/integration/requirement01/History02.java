package app.integration.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.error.MissingError;
import app.generic.TestBase;
import app.manager.QueryManager;
import app.model.LocationModel;

public class History02 extends TestBase {
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
        Mockito.doReturn(location).when(queries).getData(coords);

        // When
        var response = setupRequest("/places/{query}").pathParam("query", coords).queryParam("alias", name).post().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = setupRequest("/places").get().then();
        state.body("size()", equalTo(1));
        state.body("get(0).coords", equalTo(coords));
    }

    @Test
    public void invalid() {
        // Given
        var name = "ABCDEF";
        var coords = "180.0,360.0";
        Mockito.doThrow(new MissingError()).when(queries).getData(coords);

        // When
        var response = setupRequest("/places/{query}").pathParam("query", coords).queryParam("alias", name).post().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = setupRequest("/places").get().then();
        state.body("size()", equalTo(0));
    }
}
