package app.controller.generic;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import app.manager.AccountManager;
import app.manager.QueryManager;
import app.manager.ServiceManager;
import app.manager.SessionManager;
import app.model.AccountModel;

public abstract class BaseController {
    protected SessionManager session;
    @Autowired protected AccountManager accountManager;
    @Autowired protected ServiceManager serviceManager;
    @Autowired protected QueryManager queryManager;

    protected SessionManager getSessionFrom(HttpSession rawSession) {
        return new SessionManager(rawSession);
    }

    protected void setSessionFrom(HttpSession rawSession) {
        session = getSessionFrom(rawSession);
    }

    protected void saveAccount(AccountModel account) {
        accountManager.saveAccount(account);
        session.saveAccount(account);
    }
}