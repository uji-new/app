package app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;

@RestController
@RequestMapping("/history")
public class HistoryController extends BaseController {
    @GetMapping
    public Object getPlaces(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        return user.getHistory();
    }

    @PostMapping("/{coords}")
    public void newPlace(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        user.restoreHistoryLocation(coords);
        saveUser(user);
    }

    @DeleteMapping("/{coords}")
    public void deletePlace(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        user.removeHistoryLocation(coords);
        saveUser(user);
    }
}
