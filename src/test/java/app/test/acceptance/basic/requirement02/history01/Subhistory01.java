package app.test.acceptance.basic.requirement02.history01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero consultar información de hasta tres ubicaciones simultáneamente, con el fin de saber todos sus datos a la vez.
public class Subhistory01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var nameA = "Castellon de la Plana";
        client.location.addLocation(nameA);
        var nameB = "Alicante";
        client.location.addLocation(nameB);

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(2));
        response.body("", hasItem(hasEntry("name", nameA)));
        response.body("", hasItem(hasEntry("name", nameB)));
    }

    @Test
    public void invalid() {
        // Given
        // No locations

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("size()", equalTo(0));
    }
}
