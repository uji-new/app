package app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;
import app.error.ConfilictError;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @PostMapping
    public void newUser(HttpSession rawSession, @RequestParam String mail, @RequestParam String password) {
        setSessionFrom(rawSession);
        synchronized (mail.intern()) {
            if (accountManager.existsUser(mail))
                throw new ConfilictError();
            var user = accountManager.newUser(mail, password);
            saveUser(user);
        }
    }

    @PostMapping("/guest")
    public void newGuest(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var user = accountManager.newGuest();
        saveUser(user);
    }

    @PutMapping
    public void updateUser(HttpSession rawSession, @RequestParam String password) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        user.encryptAndSetPassword(password);
        saveUser(user);
    }

    @DeleteMapping
    public void deleteUser(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var user = session.getUser();
        accountManager.deleteUser(user.getMail());
        session.clear();
    }
}
