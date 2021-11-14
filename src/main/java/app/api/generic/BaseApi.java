package app.api.generic;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(getterVisibility = Visibility.NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseApi<T extends Enum<T>, I, O> implements Comparable<BaseApi<T, ?, ?>> {
    @JsonProperty @EqualsAndHashCode.Include @Getter private T type;
    @Getter(AccessLevel.PROTECTED) private String url;
    @Getter(AccessLevel.PROTECTED) private Map<String, String> query;

    public void setType(T type) {
        if (this.type != null)
            throw new IllegalAccessError();
        this.type = type;
    }

    public void setUrl(String url) {
        if (this.url != null)
            throw new IllegalAccessError();
        this.url = url;
    }

    public void setQuery(Map<String, String> query) {
        if (this.query != null)
            throw new IllegalAccessError();
        this.query = Collections.unmodifiableMap(query);
    }

    protected RequestSpecification setupRequest(I info) {
        return RestAssured.given().baseUri(url).queryParams(query);
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
    public int compareTo(BaseApi<T, ?, ?> other) {
        return type.compareTo(other.type);
    }
}
