package app.manager;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import app.error.AuthenticationError;
import app.manager.generic.BaseManager;
import app.manager.generic.SessionProperty;
import app.model.AccountModel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionManager extends BaseManager {
    private HttpSession session;

    private String getKeyName(SessionProperty key) {
        return key.name();
    }

    protected void put(SessionProperty key, Object value) {
        session.setAttribute(getKeyName(key), value);
    }

    protected <T> T get(SessionProperty key, Class<T> type) {
        var keyName = getKeyName(key);
        var value = session.getAttribute(keyName);
        return Optional.ofNullable(value).map(type::cast).orElseThrow();
    }

    protected void remove(SessionProperty key) {
        session.removeAttribute(getKeyName(key));
    }

    public void clear() {
        session.invalidate();
    }

    public AccountModel getAccount() {
        try {
            return get(SessionProperty.ACCOUNT, AccountModel.class);
        } catch (NoSuchElementException ignored) {
            throw new AuthenticationError();
        }
    }

    public void saveAccount(AccountModel account) {
        put(SessionProperty.ACCOUNT, account);
    }

    public void deleteAccount() {
        remove(SessionProperty.ACCOUNT);
    }
}
