package app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;

@RestController
@RequestMapping("/session")
public class SessionController extends BaseController {
    @GetMapping
    public Object getSession(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        return user;
    }

    @PostMapping
    public void newSession(HttpSession rawSession, @RequestParam String mail, @RequestParam String password) {
        setSessionFrom(rawSession);
        var user = accountManager.getUser(mail);
        user.validatePassword(password);    
        session.saveUser(user);
    }

    @DeleteMapping
    public void deleteSession(HttpSession rawSession) {
        setSessionFrom(rawSession);
        session.clear();
    }
}
