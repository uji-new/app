package app.generic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Data
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestBase {
    private SessionFilter session;
    @LocalServerPort private int port;

    protected RequestSpecification setupRequest(String path) {
        return RestAssured.given().basePath(path).port(port).filter(getSession());
    }

    @BeforeEach
    public void beforeEach() {
        setSession(new SessionFilter());
        setupRequest("/user").queryParam("mail", "mail").queryParam("password", "password").when().post().then().statusCode(200);
    }

    @AfterEach
    public void afterEach() {
        setupRequest("/user").when().delete().then().statusCode(200);
        setSession(null);
    }
}
