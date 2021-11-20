package app.manager;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import app.manager.generic.SessionProperty;
import app.model.AccountModel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionManager {
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
        return get(SessionProperty.ACCOUNT, AccountModel.class);
    }

    public void saveAccount(AccountModel account) {
        put(SessionProperty.ACCOUNT, account);
    }

    public void deleteAccount() {
        remove(SessionProperty.ACCOUNT);
    }
}
