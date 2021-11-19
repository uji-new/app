package app.test.generic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import app.client.generic.Client;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {
    @Autowired protected Client client;
    @Autowired protected Spy spy;

    protected String getId(TestInfo info) {
        var history = info.getTestClass().orElseThrow().getName();
        var test = info.getTestMethod().orElseThrow().getName();
        return String.format("%s.%s", history, test);
    }

    @Autowired
    private void setPort(@LocalServerPort int port) {
        client.setPort(port);
    }

    @BeforeEach
    public void beforeEach(TestInfo info) {
        var id = getId(info);
        client.setupSession();
        client.user.newUser(id, id);
    }

    @AfterEach
    public void afterEach() {
        client.user.deleteUser();
    }
}
