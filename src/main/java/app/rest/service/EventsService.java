package app.rest.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jcabi.aspects.Cacheable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import app.error.ServiceError;
import app.model.LocationModel;
import app.rest.service.generic.BaseService;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

@Service
@ConfigurationProperties("app.service.ticketmaster")
public class EventsService extends BaseService {
    @Override
    protected RequestSpecification setupRequest(LocationModel info) {
        return super.setupRequest(info).queryParam("latlong", info.getCoords());
    }

    protected String getLocalQuery(String path) {
        return String.format("_embedded.events*.%s", path);
    }

    protected String getImageFrom(List<Map<String, String>> images) {
        return images.stream().map(map -> map.get("url")).filter(img -> img.contains("RETINA_LANDSCAPE")).findFirst().orElseThrow(ServiceError::new);
    }

    protected Double getPriceFrom(List<Map<String, Number>> prices) {
        return prices.stream().mapToDouble(map -> map.get("min").doubleValue()).max().orElseThrow(ServiceError::new);
    }

    protected String getDateFrom(Map<String, Map<String, String>> dates) {
        var start = dates.getOrDefault("start", Map.of());
        var access = dates.getOrDefault("access", Map.of());
        return Stream.of(access.get("startDateTime"), start.get("dateTime")).filter(Objects::nonNull).findFirst().orElseThrow(ServiceError::new);
    }

    protected String getLocationFrom(List<Map<String, Map<String, String>>> locations) {
        return locations.stream().map(map -> map.get("city").get("name")).findFirst().orElseThrow(ServiceError::new);
    }

    @Override
    protected Object extractData(JsonPath body) {
        List<String> url = body.getList(getLocalQuery("url"));
        List<String> title = body.getList(getLocalQuery("name"));
        List<String> author = body.getList(getLocalQuery("promoter.name"));
        List<List<Map<String, String>>> image = body.getList(getLocalQuery("images"));
        List<List<Map<String, Number>>> price = body.getList(getLocalQuery("priceRanges"));
        List<Map<String, Map<String, String>>> date = body.getList(getLocalQuery("dates"));
        List<List<Map<String, Map<String, String>>>> location = body.getList(getLocalQuery("_embedded.venues"));
        return IntStream.range(0, title == null ? 0 : title.size()).mapToObj(i -> {
            return Map.of("title", title.get(i), "date", getDateFrom(date.get(i)), "url", url.get(i), "author", author.get(i), "image", getImageFrom(image.get(i)), "price", getPriceFrom(price.get(i)), "location", getLocationFrom(location.get(i)));
        }).toList();
    }

    @Override
    @Cacheable(lifetime = 1, unit = TimeUnit.DAYS)
    public Object getData(LocationModel info) {
        return super.getData(info);
    }
}
