package app.rest.query.generic;

import java.util.List;

import app.model.LocationModel;
import app.rest.generic.BaseRest;

public abstract class BaseQuery extends BaseRest<QueryType, String, List<LocationModel>> {
    protected LocationModel newLocation() {
        return new LocationModel();
    }
}
