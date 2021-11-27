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
        var account = session.getAccount();
        return account;
    }

    @PostMapping
    public void login(HttpSession rawSession, @RequestParam String mail, @RequestParam String password) {
        setSessionFrom(rawSession);
        var account = accountManager.getAccount(mail);
        account.validatePassword(password);
        session.saveAccount(account);
    }

    @PostMapping("/guest")
    public void loginAsGuest(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var account = accountManager.newGuest();
        session.saveAccount(account);
    }

    @DeleteMapping
    public void logout(HttpSession rawSession) {
        setSessionFrom(rawSession);
        session.clear();
    }
}
