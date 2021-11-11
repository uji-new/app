package app.acceptance.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;

import app.generic.TestBase;
import app.rest.service.generic.ServiceType;

public class History03 extends TestBase {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER;
        setupRequest("/services").queryParam("type", type).post();
        setupRequest("/places/{query}").pathParam("query", name).queryParam("alias", name).post();
        name = "Valencia";

        // When
        var response = setupRequest("/services/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.log().body();
        response.body("size()", equalTo(1));
        response.body("get(0).service.type", equalTo(type.name()));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";

        // When
        var response = setupRequest("/services/{query}").pathParam("query", name).get().then();

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
