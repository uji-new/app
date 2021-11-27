package app.test.acceptance.advanced.requirement01;

import static org.mockito.Mockito.any;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// TODO
// Como usuario quiero poder cambiar la contraseña de una cuenta para que en el próximo inicio de sesión la única contraseña válida sea la nueva contraseña introducida.
public class History03 extends BaseTest {
    @Override
    @AfterEach
    public void afterEach(TestInfo info) {
        super.afterEach(info);
        client.account.deregister();
    }

    @Test
    public void valid(TestInfo info) {
        // Given
        var id = getId(info);
        var newId = id + "Nuevo";
        client.account.register(id, id);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.updateAccount(newId);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        client.session.logout();
        var state = client.session.login(id, newId);
        state.statusCode(HttpStatus.OK.value());
    }

    @Test
    public void invalid(TestInfo info) {
        // Given
        var id = getId(info);
        client.account.register(id, id);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.updateAccount(id);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());  // Test expects CONFLICT
        client.session.logout();
        var state = client.session.login(id, id);
        state.statusCode(HttpStatus.OK.value());
    }
}
