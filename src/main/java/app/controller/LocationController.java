package app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;

@RestController
@RequestMapping("/locations")
public class LocationController extends BaseController {
    @GetMapping
    public Object getLocations(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        return account.getLocations();
    }

    @PostMapping("/{query}")
    public Object addLocation(HttpSession rawSession, @PathVariable String query, @RequestParam(required = false) String alias) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        var location = queryManager.getData(query);
        if (alias != null)
            location.setAlias(alias);
        account.addLocation(location);
        saveAccount(account);
        return location;
    }

    @PutMapping("/{coords}")
    public Object updateLocation(HttpSession rawSession, @PathVariable String coords, @RequestParam(required = false) String alias) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        var location = account.getLocation(coords);
        if (alias == null)
            alias = location.getName();
        location.setAlias(alias);
        saveAccount(account);
        return location;
    }

    @DeleteMapping("/{coords}")
    public void removeLocation(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        account.removeLocation(coords);
        saveAccount(account);
    }
}
