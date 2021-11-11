package app.integration.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;
import app.manager.QueryManager;
import app.model.LocationModel;


public class History05 extends TestBase {
    @SpyBean
    QueryManager queries;

    @Test
    public void valid() {
        // Given
        var name = "Castellon";

        var location = new LocationModel();
        location.setName(name);
        location.setLatitude(39.980);
        location.setLongitude(-0.033);
        Mockito.doReturn(location).when(queries).getData(name);
        setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post();
        var coords = setupRequest("/places").get().then().extract().jsonPath().getString("get(0).coords");
        setupRequest("/places/{query}").pathParam("query", coords).queryParam("alias", name).delete();

        // When
        var response = setupRequest("/history/{coords}").pathParam("coords", coords).post().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = setupRequest("/places").get().then();
        var stateHistory = setupRequest("/history").get().then();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        var location = new LocationModel();
        location.setName(name);
        location.setLatitude(39.980);
        location.setLongitude(-0.033);
        Mockito.doReturn(location).when(queries).getData(name);
        setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post();
        var coords = setupRequest("/places").get().then().extract().jsonPath().getString("get(0).coords");

        // When
        var response = setupRequest("/history/{coords}").pathParam("coords", coords).post().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = setupRequest("/places").get().then();
        var stateHistory = setupRequest("/history").get().then();
        statePlaces.body("size()", equalTo(1));
        stateHistory.body("size()", equalTo(0));
    }
}