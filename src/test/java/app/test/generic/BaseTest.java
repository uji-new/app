package app.test.generic;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import app.api.service.EventsService;
import app.api.service.NewsService;
import app.api.service.WeatherService;
import app.client.HistoryClient;
import app.client.PlaceClient;
import app.client.QueryClient;
import app.client.ServiceClient;
import app.client.SessionClient;
import app.client.UserClient;
import app.client.generic.BaseClient;
import app.manager.QueryManager;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {
    @Autowired private Set<BaseClient> clients;
    @Autowired protected UserClient userClient;
    @Autowired protected PlaceClient placeClient;
    @Autowired protected QueryClient queryClient;
    @Autowired protected HistoryClient historyClient;
    @Autowired protected ServiceClient serviceClient;
    @Autowired protected SessionClient sessionClient;
    @SpyBean protected QueryManager queryManager;
    @SpyBean protected WeatherService weatherService;
    @SpyBean protected EventsService eventsService;
    @SpyBean protected NewsService newsService;

    protected String getId(TestInfo info) {
        var history = info.getTestClass().orElseThrow().getName();
        var test = info.getTestMethod().orElseThrow().getName();
        return String.format("%s.%s", history, test);
    }

    @Autowired
    private void setPort(@LocalServerPort int port) {
        clients.forEach(client -> client.setPort(port));
    }

    @BeforeEach
    public void beforeEach(TestInfo info) {
        var id = getId(info);
        var session = BaseClient.newSession();
        clients.forEach(client -> client.setSession(session));
        userClient.newUser(id, id);
    }

    @AfterEach
    public void afterEach() {
        userClient.deleteUser();
    }
}
