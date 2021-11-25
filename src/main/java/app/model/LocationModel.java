package app.model;

import java.io.Serializable;
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
public class LocationModel extends BaseModel implements Comparable<LocationModel> {
    @Id @ManyToOne(fetch = FetchType.LAZY) @JsonIgnore @Setter(AccessLevel.PROTECTED) private AccountModel account;
    @Id @Getter private double latitude;
    @Id @Getter private double longitude;
    @JsonProperty @EqualsAndHashCode.Include @Getter private String name;
    @JsonProperty @Setter @Getter private String alias;

    public LocationModel(String name, double latitude, double longitude) {
        this.name = name;
        this.alias = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @JsonProperty
    @EqualsAndHashCode.Include
    public String getCoords() {
        return String.format(Locale.ROOT, "%.3f,%.3f", latitude, longitude);
    }

    @Override
    public int compareTo(LocationModel other) {
        return Comparator.comparing(LocationModel::getAlias).thenComparing(LocationModel::getName).thenComparing(LocationModel::getCoords).compare(this, other);
    }
}
