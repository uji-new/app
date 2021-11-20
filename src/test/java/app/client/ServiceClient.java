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

    public ValidatableResponse enableAllServices() {
        return setupRequest().post().then();
    }

    public ValidatableResponse disableAllServices() {
        return setupRequest().delete().then();
    }

    public ValidatableResponse enableService(String type) {
        return setupRequest().queryParam("type", type).post().then();
    }

    public ValidatableResponse disableService(String type) {
        return setupRequest().queryParam("type", type).delete().then();
    }

    public ValidatableResponse getServicesForLocation(String query) {
        return setupRequest("{query}").pathParam("query", query).get().then();
    }

    public ValidatableResponse enableAllServicesForLocation(String coords) {
        return setupRequest("{coords}").pathParam("coords", coords).post().then();
    }

    public ValidatableResponse disableAllServicesForLocation(String coords) {
        return setupRequest("{coords}").pathParam("coords", coords).delete().then();
    }

    public ValidatableResponse enableServiceForLocation(String coords, String type) {
        return setupRequest("{coords}").pathParam("coords", coords).queryParam("type", type).post().then();
    }

    public ValidatableResponse disableServiceForLocation(String coords, String type) {
        return setupRequest("{coords}").pathParam("coords", coords).queryParam("type", type).delete().then();
    }
}
