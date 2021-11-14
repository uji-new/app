package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class ServiceClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("services", path));
    }

    public ValidatableResponse getServices() {
        return setupRequest().get().then();
    }

    public ValidatableResponse newService(String type) {
        return setupRequest().queryParam("type", type).post().then();
    }

    public ValidatableResponse deleteService(String type) {
        return setupRequest().queryParam("type", type).delete().then();
    }

    public ValidatableResponse getServicesForPlace(String query) {
        return setupRequest("{query}").pathParam("query", query).get().then();
    }

    public ValidatableResponse newServiceForPlace(String coords, String type) {
        return setupRequest("{coords}").pathParam("coords", coords).queryParam("type", type).post().then();
    }

    public ValidatableResponse deleteServiceForPlace(String coords, String type) {
        return setupRequest("{coords}").pathParam("coords", coords).queryParam("type", type).delete().then();
    }
}
