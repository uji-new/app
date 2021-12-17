package app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;
import app.error.AuthenticationError;
import app.error.ConflictError;
import app.model.AccountModel;

@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {
    protected AccountModel getAccount() {
        try {
            return session.getAccount();
        } catch (AuthenticationError ignored) {
            return accountManager.newAccount();
        }
    }

    @PostMapping
    public void register(HttpSession rawSession, @RequestParam String mail, @RequestParam String password) {
        setSessionFrom(rawSession);
        var account = getAccount();
        account.setMail(mail);
        account.encryptAndSetPassword(password);
        synchronized (mail.intern()) {
            if (accountManager.existsAccount(mail))
                throw new ConflictError();
            saveAccount(account);
        }
    }

    @PutMapping
    public void updateAccount(HttpSession rawSession, @RequestParam String password) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        account.encryptAndSetPassword(password);
        saveAccount(account);
    }

    @DeleteMapping
    public void deregister(HttpSession rawSession) {
        setSessionFrom(rawSession);
        var account = session.getAccount();
        accountManager.deleteAccount(account.getMail());
        session.clear();
    }
}
