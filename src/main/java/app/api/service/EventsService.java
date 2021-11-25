package app.api.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jcabi.aspects.Cacheable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import app.api.service.generic.BaseService;
import app.error.ServiceError;
import app.model.LocationModel;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

@Service
@ConfigurationProperties("app.api.ticketmaster")
public class EventsService extends BaseService {
    @Override
    protected RequestSpecification setupRequest(LocationModel info) {
        return super.setupRequest(info).queryParam("latlong", info.getCoords());
    }

    protected String setupQuery(String path) {
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
        List<String> url = body.getList(setupQuery("url"));
        List<String> title = body.getList(setupQuery("name"));
        List<String> author = body.getList(setupQuery("promoter.name"));
        List<List<Map<String, String>>> image = body.getList(setupQuery("images"));
        List<List<Map<String, Number>>> price = body.getList(setupQuery("priceRanges"));
        List<Map<String, Map<String, String>>> date = body.getList(setupQuery("dates"));
        List<List<Map<String, Map<String, String>>>> location = body.getList(setupQuery("_embedded.venues"));
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
