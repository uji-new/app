package app.test.acceptance.advanced.requirement01;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero poder crear unas credenciales únicas que sirvan para identificarse en la aplicación.
public class History01 extends BaseTest {
    @Test
    public void valid(TestInfo info) {
        // Given
        // No user
        var id = getId(info);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.register(id, id);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
    }

    @Test
    public void invalid(TestInfo info) {
        // Given
        var id = getId(info);
        client.account.register(id, id);
        client.session.logout();
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.register(id, id);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.CONFLICT.value());
    }
}
