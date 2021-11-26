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
        var account = session.getAccount();
        var services = account.getServices();
        return serviceManager.getServices().stream().map(service -> {
            var type = service.getType();
            var enabled = services.contains(type);
            return Map.of("service", service, "enabled", enabled);
        }).toList();
    }

    @PostMapping
    public void enableService(HttpSession rawSession, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        if (type == null) account.enableAllServices();
        else account.enableService(type);
        saveAccount(account);
    }

    @DeleteMapping
    public void disableService(HttpSession rawSession, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        if (type == null) account.disableAllServices();
        else account.disableService(type);
        saveAccount(account);
    }

    protected LocationModel getLocation(String query) {
        var account = session.getAccount();
        LocationModel location;
        try {
            location = account.getLocation(query);
        } catch (MissingError ignored) {
            location = queryManager.getData(query);
            location.setServices(account.getServices());
        }
        return location;
    }

    @GetMapping("/{query}")
    public Object getServicesForLocation(HttpSession rawSession, @PathVariable String query) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        var location = getLocation(query);
        var services = location.getServices();
        return account.getServices().stream().parallel().map(type -> {
            var service = serviceManager.getService(type);
            var enabled = services.contains(type);
            Object data = false;
            if (enabled) try {
                data = service.getData(location);
            } catch (AssertionError ignored) {}
            return Map.of("service", service, "enabled", enabled, "data", data);
        }).toList();
    }

    @PostMapping("/{coords}")
    public void enableServiceForLocation(HttpSession rawSession, @PathVariable String coords, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        var location = account.getLocation(coords);
        if (type == null) location.enableAllServices();
        else location.enableService(type);
        saveAccount(account);
    }

    @DeleteMapping("/{coords}")
    public void disableServiceForLocation(HttpSession rawSession, @PathVariable String coords, @RequestParam(required = false) ServiceType type) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        var location = account.getLocation(coords);
        if (type == null) location.disableAllServices();
        else location.disableService(type);
        saveAccount(account);
    }
}
