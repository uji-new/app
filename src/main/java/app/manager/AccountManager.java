package app.manager;

import java.util.List;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import app.dao.UserDao;
import app.error.AuthenticationError;
import app.model.AccountModel;

@Service
public class AccountManager {
    @Autowired private UserDao userDao;

    public AccountModel newGuest() {
        var user = new AccountModel();
        user.setTransient(true);
        return user;
    }

    public AccountModel newUser(String mail, String password) {
        return new AccountModel(mail, password);
    }

    public boolean existsUser(String mail) {
        return userDao.existsById(mail);
    }

    public List<AccountModel> getUsers() {
        return userDao.findAll();
    }

    public AccountModel getUser(String mail) {
        return userDao.findById(mail).orElseThrow(AuthenticationError::new);
    }

    public void saveUser(AccountModel user) {
        if (!user.isTransient())
            userDao.save(user);
    }

    public void deleteUser(String mail) {
        userDao.deleteById(mail);
    }

    @Bean
    public PasswordEncryptor newEncryptor() {
        return new BasicPasswordEncryptor();
    }
}
