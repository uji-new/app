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

// Como usuario quiero poder activar servicios de información (API) independientes para cada ubicación, con el doble fin de consultar sólo información de interés y contribuir a la gestión eficiente de recursos.
public class Subhistory01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellón";
        var type = ServiceType.WEATHER.name();
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.service.enableService(type);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.service.enableServiceForLocation(coords, type);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var status = client.service.getServicesForLocation(coords);
        status.body(setupEnabledQuery(true, ""), hasSize(1));
        status.body(setupEnabledQuery(true, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellón";
        var type = ServiceType.WEATHER.name();
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.service.enableService(type);
        Mockito.reset(spy.accountManager);
        type = "INVALIDO";

        // When
        var response = client.service.enableServiceForLocation(coords, type);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        var status = client.service.getServicesForLocation(coords);
        status.body(setupEnabledQuery(true, ""), hasSize(0));
    }
}
