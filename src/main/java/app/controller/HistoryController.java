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
    public Object getLocations(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        return account.getHistory();
    }

    @PostMapping("/{coords}")
    public void restoreLocation(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        account.restoreHistoryLocation(coords);
        saveAccount(account);
    }

    @DeleteMapping("/{coords}")
    public void removeLocation(HttpSession rawSession, @PathVariable String coords) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        account.removeHistoryLocation(coords);
        saveAccount(account);
    }
}
