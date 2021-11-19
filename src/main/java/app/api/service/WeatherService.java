package app.api.service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.jcabi.aspects.Cacheable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import app.api.service.generic.BaseService;
import app.model.LocationModel;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

@Service
@ConfigurationProperties("app.api.openweathermap")
public class WeatherService extends BaseService {
    private String icon;

    public void setIcon(String icon) {
        if (this.icon != null)
            throw new IllegalAccessError();
        this.icon = icon;
    }

    @Override
    protected RequestSpecification setupRequest(LocationModel info) {
        return super.setupRequest(info).queryParam("lat", info.getLatitude()).queryParam("lon", info.getLongitude());
    }

    protected URI getIconFrom(String icon) {
        return UriComponentsBuilder.fromUriString(this.icon).build(icon);
    }

    @Override
    protected Object extractData(JsonPath body) {
        return Map.of(
            "temp", body.getDouble("daily[0].temp.day"),
            "rain", body.getDouble("daily[0].pop"),
            "wind", body.getDouble("daily[0].wind_speed"),
            "icon", getIconFrom(body.getString("daily[0].weather[0].icon")),
            "description", body.getString("daily[0].weather[0].description"));    
    }

    @Override
    @Cacheable(lifetime = 30, unit = TimeUnit.MINUTES)
    public Object getData(LocationModel info) {
        return super.getData(info);
    }
}
