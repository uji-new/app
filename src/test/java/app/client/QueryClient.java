package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class QueryClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("query", path));
    }

    public ValidatableResponse query(String query) {
        return setupRequest("{query}").pathParam("query", query).get().then();
    }
}
