package app.test.integration.basic.requirement02.history04;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información de cualquiera de las ubicaciones activas por separado.
public class Subhistory01 extends SessionTest {
    protected String getQueryFiltering(String coords, String path) {
        return String.format("find{it.coords=='%s'}.%s", coords, path);
    }

    @Test
    public void valid() {
        // Given
        var nameA = "Valencia";
        var locationMockA = new LocationModel(nameA, 39.503, -0.405);
        Mockito.doReturn(locationMockA).when(spy.queryManager).getData(nameA);
        client.location.addLocation(nameA);

        var nameB = "Castellon";
        var locationMockB = new LocationModel(nameB, 39.980, -0.033);
        Mockito.doReturn(locationMockB).when(spy.queryManager).getData(nameB);
        var location = client.location.addLocation(nameB);
        var coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(2));
        response.body(getQueryFiltering(coords, "name"), equalTo(nameB));
        response.body(getQueryFiltering(coords, "alias"), equalTo(nameB));
        response.body(getQueryFiltering(coords, "coords"), equalTo(coords));
    }

    @Test
    public void invalid() {
        // Given
        var nameA = "Valencia";
        var locationMockA = new LocationModel(nameA, 39.503, -0.405);
        Mockito.doReturn(locationMockA).when(spy.queryManager).getData(nameA);
        var location = client.location.addLocation(nameA);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        var nameB = "Castellon";
        var locationMockB = new LocationModel(nameB, 39.980, -0.033);
        Mockito.doReturn(locationMockB).when(spy.queryManager).getData(nameB);
        location = client.location.addLocation(nameB);
        coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(0));
    }
}
