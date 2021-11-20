package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class AccountClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("account", path));
    }

    public ValidatableResponse register(String mail, String password) {
        return setupRequest().queryParam("mail", mail).queryParam("password", password).post().then();
    }

    public ValidatableResponse updateAccount(String password) {
        return setupRequest().queryParam("password", password).put().then();
    }

    public ValidatableResponse deregister() {
        return setupRequest().delete().then();
    }
}
