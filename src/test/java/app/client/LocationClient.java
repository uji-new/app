package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class LocationClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("locations", path));
    }

    public ValidatableResponse getLocations() {
        return setupRequest().get().then();
    }

    public ValidatableResponse addLocation(String query, String alias) {
        return setupRequest("{query}").pathParam("query", query).queryParam("alias", alias).post().then();
    }

    public ValidatableResponse updateLocation(String coords, String alias) {
        return setupRequest("{coords}").pathParam("coords", coords).queryParam("alias", alias).put().then();
    }

    public ValidatableResponse removeLocation(String coords) {
        return setupRequest("{coords}").pathParam("coords", coords).delete().then();
    }
}
