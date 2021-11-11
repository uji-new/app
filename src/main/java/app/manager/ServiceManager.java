package app.manager;

import java.util.SortedSet;

import com.jcabi.aspects.Cacheable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.rest.service.generic.BaseService;
import app.rest.service.generic.ServiceType;
import lombok.Data;

@Data
@Service
public class ServiceManager {
    @Autowired private SortedSet<BaseService> services;

    @Cacheable(forever = true)
    public BaseService getService(ServiceType type) {
        return services.stream().filter(service -> service.getType().equals(type)).findFirst().orElseThrow();
    }
}
