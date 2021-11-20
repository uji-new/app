package app.client.generic;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.client.HistoryClient;
import app.client.LocationClient;
import app.client.QueryClient;
import app.client.ServiceClient;
import app.client.SessionClient;
import app.client.AccountClient;
import io.restassured.filter.Filter;
import io.restassured.filter.session.SessionFilter;

@Service
public class Client {
    @Autowired private Set<BaseClient> clients;
    @Autowired public AccountClient account;
    @Autowired public LocationClient location;
    @Autowired public QueryClient query;
    @Autowired public HistoryClient history;
    @Autowired public ServiceClient service;
    @Autowired public SessionClient session;

    protected Filter newSession() {
        return new SessionFilter();
    }

    public void setPort(int port) {
        clients.forEach(client -> client.setPort(port));
    }

    public void setupSession() {
        var session = newSession();
        clients.forEach(client -> client.setSession(session));
    }
}
