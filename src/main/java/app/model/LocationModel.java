package app.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.model.generic.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
class LocationId implements Serializable {
    UserModel user;
    private double latitude;
    private double longitude;
}

@Data
@Entity
@NoArgsConstructor
@IdClass(LocationId.class)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class LocationModel extends BaseModel implements Comparable<LocationModel> {
    @Id @ManyToOne(fetch = FetchType.LAZY) @ToString.Exclude @JsonIgnore private UserModel user;
    @Id private double latitude;
    @Id private double longitude;
    @EqualsAndHashCode.Include private String name;
    private String alias;

    public LocationModel(double latitude, double longitude, String name) {
        setName(name);
        setAlias(name);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    @EqualsAndHashCode.Include
    public String getCoords() {
        return String.format(Locale.ROOT, "%.3f,%.3f", latitude, longitude);
    }

    @Override
    public int compareTo(LocationModel other) {
        return Comparator.comparing(LocationModel::getAlias).thenComparing(LocationModel::getName).thenComparing(LocationModel::getCoords).compare(this, other);
    }
}
