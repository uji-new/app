package app.manager;

import java.util.List;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import app.dao.UserDao;
import app.error.AuthenticationError;
import app.model.UserModel;

@Service
public class AccountManager {
    @Autowired private UserDao userDao;

    public UserModel newGuest() {
        var user = new UserModel();
        user.setGuest(true);
        return user;
    }

    public UserModel newUser(String mail, String password) {
        return new UserModel(mail, password);
    }

    public boolean existsUser(String mail) {
        return userDao.existsById(mail);
    }

    public List<UserModel> getUsers() {
        return userDao.findAll();
    }

    public UserModel getUser(String mail) {
        return userDao.findById(mail).orElseThrow(AuthenticationError::new);
    }

    public void saveUser(UserModel user) {
        if (!user.isGuest())
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
