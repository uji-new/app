package app.test.acceptance.basic.requirement02.history02;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero poder desactivar servicios de información (API) independientes para cada ubicación, con el doble fin de consultar sólo información de interés y contribuir a la gestión eficiente de recursos.
public class Subhistory02 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.reset(spy.accountManager);

        // When
        var response = client.service.disableServiceForLocation(coords, type);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var status = client.service.getServicesForLocation(coords);
        status.body(setupActiveQuery(""), hasSize(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        var typeA = ServiceType.WEATHER.name();
        client.service.enableService(typeA);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        Mockito.reset(spy.accountManager);
        var typeB = "INVALIDO";

        // When
        var response = client.service.disableServiceForLocation(coords, typeB);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        var status = client.service.getServicesForLocation(coords);
        status.body(setupActiveQuery(""), hasSize(1));
        status.body(setupActiveQuery("service.type"), hasItem(typeA));
    }
}
