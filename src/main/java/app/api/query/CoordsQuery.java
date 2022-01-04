package app.api.query;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import app.api.query.generic.BaseQuery;
import app.model.LocationModel;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import com.jcabi.aspects.Cacheable;

@Service
@ConfigurationProperties("app.api.geocode")
public class CoordsQuery extends BaseQuery {
    @Override
    protected RequestSpecification setupRequest(String info) {
        return super.setupRequest(info).queryParam("locate", info);
    }

    @Override
    protected ValidatableResponse validateResponse(ValidatableResponse response) {
        return super.validateResponse(response).body("error.code", anyOf(equalTo(null), equalTo("008")));
    }

    @Override
    protected List<LocationModel> extractData(JsonPath body) {
        var name = body.getString("standard.city");
        if (name == null) name = body.getString("city");
        var latitude = body.getDouble("latt");
        var longitude = body.getDouble("longt");
        return name == null ? List.of() : List.of(newLocation(name, latitude, longitude));
    }

    @Override
    @Cacheable(forever = true)
    public List<LocationModel> getData(String info) {
        return super.getData(info);
    }
}
