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
        var user = session.getUser();
        return user.getLocations();
    }

    @PostMapping("/{query}")
    public void addLocation(HttpSession rawSession, @PathVariable String query, @RequestParam String alias) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = queryManager.getData(query);
        location.setAlias(alias);
        user.addLocation(location);
        saveUser(user);
    }

    @PutMapping("/{coords}")
    public void updateLocation(HttpSession rawSession, @PathVariable String coords, @RequestParam String alias) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = user.getLocation(coords);
        location.setAlias(alias);
        saveUser(user);
    }

    @DeleteMapping("/{coords}")
    public void removeLocation(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        user.removeLocation(coords);
        saveUser(user);
    }
}
