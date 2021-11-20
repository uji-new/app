package app.model.generic;

import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.SortedSet;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.SortNatural;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import app.api.service.generic.BaseService;
import app.api.service.generic.ServiceType;
import app.manager.ServiceManager;

@MappedSuperclass
@Configurable(preConstruction = true)
public abstract class BaseModel {
    @ElementCollection(fetch = FetchType.EAGER) @SortNatural private SortedSet<ServiceType> services = new TreeSet<>();
    @Autowired @Transient ServiceManager serviceManager;

    public void addAllServices() {
        setServices(serviceManager.getServices().stream().map(BaseService::getType).collect(Collectors.toCollection(TreeSet::new)));
    }

    public void removeAllServices() {
        services.clear();
    }

    public void setServices(SortedSet<ServiceType> services) {
        removeAllServices();
        this.services.addAll(services);
    }

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
