package app.test.acceptance.basic.requirement02.history04;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.util.Collections;

import static org.hamcrest.Matchers.everyItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información de eventos sobre un una ubicación activa.
public class Subhistory03 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.EVENTS.name();
        client.service.enableService(type);

        var name = "Valencia";
        client.location.addLocation(name);

        name = "Castellon";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data.title"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.date"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.url"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.author"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.image"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.price"), everyItem(instanceOf(Number.class)));
        response.body(setupServiceQuery(type, "data.location"), everyItem(instanceOf(String.class)));
    }

    @Test
    public void invalid() {
        // Given
        var type = ServiceType.EVENTS.name();
        client.service.enableService(type);

        var name = "Valencia";
        client.location.addLocation(name);

        var alias = "Antarctica";
        var coords = "-78.159,16.406";
        var location = client.location.addLocation(coords, alias);
        coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data"), equalTo(Collections.emptyList()));
    }
}
