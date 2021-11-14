package app.manager;

import java.util.SortedSet;

import com.jcabi.aspects.Cacheable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.api.service.generic.BaseService;
import app.api.service.generic.ServiceType;
import lombok.Getter;

@Service
public class ServiceManager {
    @Autowired @Getter private SortedSet<BaseService> services;

    @Cacheable(forever = true)
    public BaseService getService(ServiceType type) {
        return services.stream().filter(service -> service.getType().equals(type)).findFirst().orElseThrow();
    }
}
