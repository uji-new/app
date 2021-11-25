package app.test.acceptance.basic.requirement02;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información de cualquiera de las ubicaciones activas por separado.
public class History04 extends SessionTest {
    protected String getQueryFiltering(String coords, String path) {
        return String.format("find{it.coords=='%s'}.%s", coords, path);
    }

    @Test
    public void valid() {
        // Given
        var nameA = "Valencia";
        client.location.addLocation(nameA);

        var nameB = "Castellon de la Plana";
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
        var location = client.location.addLocation(nameA);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        var nameB = "Castellon de la Plana";
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
