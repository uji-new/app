package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class SessionClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("session", path));
    }

    public ValidatableResponse getSession() {
        return setupRequest().get().then();
    }

    public ValidatableResponse newSession(String mail, String password) {
        return setupRequest().queryParam("mail", mail).queryParam("password", password).post().then();
    }

    public ValidatableResponse newGuest() {
        return setupRequest("guest").post().then();
    }

    public ValidatableResponse deleteSession() {
        return setupRequest().delete().then();
    }
}
