package app.rest.generic;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonAutoDetect(getterVisibility = Visibility.NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseRest<T extends Enum<T>, I, O>  implements Comparable<BaseRest<T, ?, ?>> {
    @JsonProperty @EqualsAndHashCode.Include private T type;
    private String url;
    private Map<String, String> query;

    protected RequestSpecification setupRequest(I info) {
        return RestAssured.given().baseUri(getUrl()).queryParams(getQuery());
    }

    protected ValidatableResponse validateResponse(ValidatableResponse response) {
        return response.statusCode(200);
    }

    protected abstract O extractData(JsonPath body);

    public O getData(I info) {
        var request = setupRequest(info);
        var response = request.get().then();
        response = validateResponse(response);
        var body = response.extract().jsonPath();
        return extractData(body);
    }

    @Override
    public int compareTo(BaseRest<T, ?, ?> other) {
        return type.compareTo(other.type);
    }
}
