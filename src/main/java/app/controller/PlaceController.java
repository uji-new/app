package app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;
import app.manager.QueryManager;

@RestController
@RequestMapping("/places")
public class PlaceController extends BaseController {
    @Autowired protected QueryManager queries;

    @GetMapping
    public Object getPlaces(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        return user.getLocations();
    }

    @PostMapping("/{query}")
    public void newPlace(HttpSession rawSession, @PathVariable String query, @RequestParam String alias) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = queries.getData(query);
        location.setAlias(alias);
        user.addLocation(location);
        saveUser(user);
    }

    @PutMapping("/{coords}")
    public void updatePlace(HttpSession rawSession, @PathVariable String coords, @RequestParam String alias) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        var location = user.getLocation(coords);
        location.setAlias(alias);
        saveUser(user);
    }

    @DeleteMapping("/{coords}")
    public void deletePlace(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        user.removeLocation(coords);
        saveUser(user);
    }
}
