package app.api.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.jcabi.aspects.Cacheable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import app.api.service.generic.BaseService;
import app.model.LocationModel;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

@Service
@ConfigurationProperties("app.api.newsdata")
public class NewsService extends BaseService {
    @Override
    protected RequestSpecification setupRequest(LocationModel info) {
        // Restrictive and inaccurate
        return super.setupRequest(info).queryParam("q", info.getName());
    }

    protected String setupQuery(String path) {
        return String.format("results.%s", path);
    }

    public String getAuthorFrom(String author) {
        return author.substring(0, 1).toUpperCase() + author.substring(1);
    }

    @Override
    protected Object extractData(JsonPath body) {
        List<String> title = body.getList(setupQuery("title"));
        List<String> description = body.getList(setupQuery("description"));
        List<String> url = body.getList(setupQuery("link"));
        List<String> author = body.getList(setupQuery("source_id"));
        List<String> image = body.getList(setupQuery("image_url"));
        return IntStream.range(0, title.size()).mapToObj(i -> {
            var imgRaw = image.get(i);
            Object img = imgRaw == null ? false : imgRaw;
            return Map.of("title", title.get(i), "description", description.get(i), "url", url.get(i), "author", getAuthorFrom(author.get(i)), "image", img);
        }).toList();
    }

    @Override
    @Cacheable(lifetime = 1, unit = TimeUnit.DAYS)
    public Object getData(LocationModel info) {
        return super.getData(info);
    }
}
