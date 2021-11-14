package app.client;

import org.springframework.stereotype.Service;

import app.client.generic.BaseClient;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Service
public class HistoryClient extends BaseClient {
    @Override
    protected RequestSpecification setupRequest(String... path) {
        return super.setupRequest(prefixArgs("history", path));
    }

    public ValidatableResponse getPlaces() {
        return setupRequest().get().then();
    }

    public ValidatableResponse newPlace(String coords) {
        return setupRequest("{coords}").pathParam("coords", coords).post().then();
    }

    public ValidatableResponse deletePlace(String coords) {
        return setupRequest("{coords}").pathParam("coords", coords).delete().then();
    }
}
