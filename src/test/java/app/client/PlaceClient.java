package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class PlaceClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("places", path));
    }

    public ValidatableResponse getPlaces() {
        return setupRequest().get().then();
    }

    public ValidatableResponse newPlace(String query, String alias) {
        return setupRequest("{query}").pathParam("query", query).queryParam("alias", alias).post().then();
    }

    public ValidatableResponse updatePlace(String coords, String alias) {
        return setupRequest("{coords}").pathParam("coords", coords).queryParam("alias", alias).put().then();
    }

    public ValidatableResponse deletePlace(String coords) {
        return setupRequest("{coords}").pathParam("coords", coords).delete().then();
    }
}
