package app.manager;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import app.manager.generic.SessionProperty;
import app.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionManager {
    private HttpSession session;

    private String getKeyName(SessionProperty key) {
        return key.name();
    }

    protected void put(SessionProperty key, Object value) {
        getSession().setAttribute(getKeyName(key), value);
    }

    protected <T> T get(SessionProperty key, Class<T> type) {
        var keyName = getKeyName(key);
        var value = getSession().getAttribute(keyName);
        return Optional.ofNullable(value).map(type::cast).orElseThrow();
    }

    protected void remove(SessionProperty key) {
        getSession().removeAttribute(getKeyName(key));
    }

    public void clear() {
        getSession().invalidate();
    }

    public UserModel getUser() {
        return get(SessionProperty.USER, UserModel.class);
    }

    public void saveUser(UserModel user) {
        put(SessionProperty.USER, user);
    }

    public void deleteUser() {
        remove(SessionProperty.USER);
    }
}
