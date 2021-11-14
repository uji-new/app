package app.client.generic;

import java.util.stream.Stream;

import org.springframework.web.util.UriComponentsBuilder;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.specification.RequestSpecification;
import lombok.Setter;

public abstract class BaseClient {
    @Setter private Filter session;
    @Setter private int port;

    public static Filter newSession() {
        return new SessionFilter();
    }

    protected String[] prefixArgs(String prefix, String... args) {
        return Stream.concat(Stream.of(prefix), Stream.of(args)).toArray(String[]::new);
    }

    protected RequestSpecification setupRequest(String... path) {
        var url = UriComponentsBuilder.newInstance().pathSegment(path).build();
        return RestAssured.given().basePath(url.toString()).port(port).filter(session);
    }
}
