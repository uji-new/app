package app.api.service.generic;

import com.fasterxml.jackson.annotation.JsonProperty;

import app.api.generic.BaseApi;
import app.model.LocationModel;
import lombok.Getter;

@Getter
public abstract class BaseService extends BaseApi<ServiceType, LocationModel, Object> {
    @JsonProperty private String name;
    @JsonProperty private String description;

    public void setName(String name) {
        if (this.name != null)
            throw new IllegalAccessError();
        this.name = name;
    }

    public void setDescription(String description) {
        if (this.description != null)
            throw new IllegalAccessError();
        this.description = description;
    }
}
