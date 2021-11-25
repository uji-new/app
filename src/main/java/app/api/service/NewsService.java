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

@Service
@ConfigurationProperties("app.api.currents")
public class NewsService extends BaseService {
    protected String getQueryFiltering(String path) {
        return String.format("news.findAll{it.title.length() < it.description.length()}.%s", path);
    }

    @Override
    protected Object extractData(JsonPath body) {
        List<String> title = body.getList(getQueryFiltering("title"));
        List<String> description = body.getList(getQueryFiltering("description"));
        List<String> url = body.getList(getQueryFiltering("url"));
        List<String> author = body.getList(getQueryFiltering("author"));
        List<String> image = body.getList(getQueryFiltering("image"));
        return IntStream.range(0, title.size()).mapToObj(i -> {
            var authRaw = author.get(i);
            var imgRaw = image.get(i);
            Object auth = authRaw.equals("") ? false : authRaw;
            Object img = imgRaw.equals("None") ? false : imgRaw;
            return Map.of("title", title.get(i), "description", description.get(i), "url", url.get(i), "author", auth, "image", img);
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
