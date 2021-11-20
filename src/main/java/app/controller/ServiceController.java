package app.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.api.service.generic.ServiceType;
import app.controller.generic.BaseController;
import app.error.MissingError;
import app.model.LocationModel;

@RestController
@RequestMapping("/services")
public class ServiceController extends BaseController {
    @GetMapping
    public Object getServices(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var userServices = user.getServices();
        return serviceManager.getServices().stream().map(service -> {
            var type = service.getType();
            var active = userServices.contains(type);
            return Map.of("service", service, "active", active);
        }).toList();
    }

    @PostMapping
    public void addService(HttpSession rawSession, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        if (type == null) user.addAllServices();
        else user.addService(type);
        saveUser(user);
    }

    @DeleteMapping
    public void removeService(HttpSession rawSession, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        if (type == null) user.removeAllServices();
        else user.removeService(type);
        saveUser(user);
    }

    protected LocationModel getLocation(String query) {
        var user = session.getUser();
        LocationModel location;
        try {
            location = user.getLocation(query);
        } catch (MissingError ignored) {
            location = queryManager.getData(query);
            location.setServices(user.getServices());
        }
        return location;
    }

    @GetMapping("/{query}")
    public Object getServicesForLocation(HttpSession rawSession, @PathVariable String query) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = getLocation(query);
        var locationServices = location.getServices();
        return user.getServices().stream().parallel().map(type -> {
            var service = serviceManager.getService(type);
            var active = locationServices.contains(type);
            Object data = false;
            if (active) try {
                data = service.getData(location);
            } catch (AssertionError ignored) {}
            return Map.of("service", service, "active", active, "data", data);
        }).toList();
    }

    @PostMapping("/{coords}")
    public void addServiceForLocation(HttpSession rawSession, @PathVariable String coords, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = user.getLocation(coords);
        if (type == null) location.addAllServices();
        else location.addService(type);
        saveUser(user);
    }

    @DeleteMapping("/{coords}")
    public void removeServiceForLocation(HttpSession rawSession, @PathVariable String coords, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = user.getLocation(coords);
        if (type == null) location.removeAllServices();
        else location.removeService(type);
        saveUser(user);
    }
}
