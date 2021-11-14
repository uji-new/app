package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class UserClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("user", path));
    }

    public ValidatableResponse newUser(String mail, String password) {
        return setupRequest().queryParam("mail", mail).queryParam("password", password).post().then();
    }

    public ValidatableResponse updateUser(String password) {
        return setupRequest().queryParam("password", password).put().then();
    }

    public ValidatableResponse deleteUser() {
        return setupRequest().delete().then();
    }
}
