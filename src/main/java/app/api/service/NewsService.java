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
@ConfigurationProperties("app.api.newsapi")
public class NewsService extends BaseService {
    @Override
    protected RequestSpecification setupRequest(LocationModel info) {
        // Too restrictive and inaccurate
        // ...queryParam("q", info.getName())
        return super.setupRequest(info);
    }

    protected String setupQuery(String path) {
        return String.format("articles.%s", path);
    }

    @Override
    protected Object extractData(JsonPath body) {
        List<String> title = body.getList(setupQuery("title"));
        List<String> description = body.getList(setupQuery("description"));
        List<String> url = body.getList(setupQuery("url"));
        List<String> author = body.getList(setupQuery("author"));
        List<String> image = body.getList(setupQuery("url2Image"));
        return IntStream.range(0, title.size()).mapToObj(i -> {
            return Map.of("title", title.get(i), "description", description.get(i), "url", url.get(i), "author", author.get(i), "image", image.get(i));
        }).toList();
    }

    @Cacheable(lifetime = 1, unit = TimeUnit.DAYS)
    public Object getGlobalData() {
        return super.getData(null);
    }

    @Override
    public Object getData(LocationModel info) {
        return getGlobalData();
    }    
}
