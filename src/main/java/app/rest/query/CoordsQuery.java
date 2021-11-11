package app.rest.query;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import app.model.LocationModel;
import app.rest.query.generic.BaseQuery;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import com.jcabi.aspects.Cacheable;

@Service
@ConfigurationProperties("app.query.geocode")
public class CoordsQuery extends BaseQuery {
    @Override
    protected RequestSpecification setupRequest(String info) {
        return super.setupRequest(info).queryParam("locate", info);
    }

    @Override
    protected List<LocationModel> extractData(JsonPath body) {
        var name = body.getString("city");
        var latitude = body.getDouble("latt");
        var longitude = body.getDouble("longt");
        if (name == null) return List.of();

        var location = newLocation();
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return List.of(location);
    }

    @Override
    @Cacheable(forever = true)
    public List<LocationModel> getData(String info) {
        return super.getData(info);
    }
}
