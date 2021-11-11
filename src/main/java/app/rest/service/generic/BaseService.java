package app.rest.service.generic;

import com.fasterxml.jackson.annotation.JsonProperty;

import app.model.LocationModel;
import app.rest.generic.BaseRest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class BaseService extends BaseRest<ServiceType, LocationModel, Object> {
    @JsonProperty private String name;
    @JsonProperty private String description;
}
