package app.model.generic;

import java.util.TreeSet;
import java.util.SortedSet;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.SortNatural;

import app.rest.service.generic.ServiceType;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseModel {
    @ElementCollection(fetch = FetchType.EAGER) @SortNatural private SortedSet<ServiceType> services = new TreeSet<>();

    public void addService(ServiceType type) {
        services.add(type);
    }

    public void removeService(ServiceType type) {
        services.remove(type);
    }
}
