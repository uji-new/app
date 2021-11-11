package app.model;

import java.util.TreeSet;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.jcabi.aspects.Cacheable;

import org.hibernate.annotations.SortNatural;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

import app.error.AuthenticationError;
import app.error.MissingError;
import app.model.generic.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserModel extends BaseModel {
    @Id private String mail;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) @SortNatural private SortedSet<LocationModel> locations = new TreeSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) @SortNatural private SortedSet<LocationModel> history = new TreeSet<>();

    @Cacheable(forever = true)
    protected PasswordEncryptor getEncryptor() {
        return new BasicPasswordEncryptor();
    }

    public void encryptAndSetPassword(String password) {
        setPassword(getEncryptor().encryptPassword(password));
    }

    public void validatePassword(String password) {
        if (!getEncryptor().checkPassword(password, getPassword()))
            throw new AuthenticationError();
    }

    public LocationModel newLocation() {
        return new LocationModel();
    }

    public void addLocation(LocationModel location) {
        location.setUser(this);
        locations.add(location);
    }

    protected LocationModel getLocationFrom(SortedSet<LocationModel> locations, String coords) {
        return locations.stream().filter(loc -> loc.getCoords().equals(coords)).findFirst().orElseThrow(MissingError::new);
    }

    public LocationModel getLocation(String coords) {
        return getLocationFrom(locations, coords);
    }

    public void removeLocation(String coords) {
        var location = getLocation(coords);
        locations.remove(location);
        history.add(location);
    }

    public LocationModel getHistoryLocation(String coords) {
        return getLocationFrom(history, coords);
    }

    public void restoreHistoryLocation(String coords) {
        var location = getHistoryLocation(coords);
        removeHistoryLocation(coords);
        addLocation(location);
    }

    public void removeHistoryLocation(String coords) {
        var location = getHistoryLocation(coords);
        history.remove(location);
    }
}
