package app.model.generic;

import java.util.TreeSet;
import java.util.Collections;
import java.util.SortedSet;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.SortNatural;

import app.api.service.generic.ServiceType;
import lombok.Setter;

@MappedSuperclass
public abstract class BaseModel {
    @ElementCollection(fetch = FetchType.EAGER) @SortNatural @Setter private SortedSet<ServiceType> services = new TreeSet<>();

    public SortedSet<ServiceType> getServices() {
        return Collections.unmodifiableSortedSet(services);
    }

    public void addService(ServiceType type) {
        services.add(type);
    }

    public void removeService(ServiceType type) {
        services.remove(type);
    }
}
