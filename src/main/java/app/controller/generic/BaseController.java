package app.controller.generic;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import app.manager.AccountManager;
import app.manager.SessionManager;
import app.model.UserModel;
import lombok.Data;

@Data
public abstract class BaseController {
    protected SessionManager session;
    @Autowired protected AccountManager accounts;

    protected SessionManager getSessionFrom(HttpSession rawSession) {
        return new SessionManager(rawSession);
    }

    protected void setSessionFrom(HttpSession rawSession) {
        setSession(getSessionFrom(rawSession));
    }

    protected void saveUser(UserModel user) {
        accounts.saveUser(user);
        session.saveUser(user);
    }
}