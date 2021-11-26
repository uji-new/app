package app.test.integration.basic.requirement03;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import static org.hamcrest.Matchers.hasItem;


import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// TODO
// Como usuario quiero desactivar un servicio de información que haya dejado de interesar, con el fin de evitar interfaces de usuario sobrecargadas.
public class History04 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.service.disableService(type);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.service.getServices();
        state.body(setupEnabledQuery(false, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        var type = "INVALIDO";
        Mockito.reset(spy.accountManager);

        // When
        var response = client.service.enableService(type);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
