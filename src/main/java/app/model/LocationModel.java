package app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.model.generic.BaseModel;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@EqualsAndHashCode
class LocationId implements Serializable {
    private AccountModel account;
    private double latitude;
    private double longitude;
}

@Entity
@NoArgsConstructor
@IdClass(LocationId.class)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class LocationModel extends BaseModel implements Comparable<LocationModel>, Cloneable {
    @ManyToOne(fetch = FetchType.LAZY) @JsonIgnore @Setter(AccessLevel.PROTECTED) private AccountModel account;
    @Id @EqualsAndHashCode.Include @Getter private double latitude;
    @Id @EqualsAndHashCode.Include @Getter private double longitude;
    @JsonProperty @Getter private String name;
    @JsonProperty @Setter @Getter private String alias;
    protected final static int scaleCoord = 3;

    public LocationModel(String name, double latitude, double longitude) {
        this.name = name;
        this.alias = name;
        this.latitude = roundCoord(latitude);
        this.longitude = roundCoord(longitude);
    }

    protected double roundCoord(double coord) {
        return new BigDecimal(coord).setScale(scaleCoord, RoundingMode.HALF_UP).doubleValue();
    }

    @JsonProperty
    public String getCoords() {
        var format = String.format("%%.%df,%%.%df", scaleCoord, scaleCoord);
        return String.format(Locale.ROOT, format, latitude, longitude);
    }

    @Override
    public int compareTo(LocationModel other) {
        return Comparator.comparing(LocationModel::getAlias).thenComparing(LocationModel::getName).thenComparing(LocationModel::getCoords).compare(this, other);
    }

    public Object clone() {
        return new LocationModel(name, latitude, longitude);
    }
}
