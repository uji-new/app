package app.manager;

import java.util.List;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.error.MissingError;
import app.model.LocationModel;
import app.rest.query.generic.BaseQuery;

@Service
public class QueryManager {
    @Autowired private SortedSet<BaseQuery> services;

    public List<LocationModel> getAllData(String query) {
        return services.stream().parallel().flatMap(service -> service.getData(query).stream()).toList();
    }

    public LocationModel getData(String info) {
        return getAllData(info).stream().findFirst().orElseThrow(MissingError::new);
    }
}
