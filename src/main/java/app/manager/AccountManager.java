package app.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dao.UserDao;
import app.error.AuthenticationError;
import app.model.UserModel;

@Service
public class AccountManager {
    @Autowired private UserDao userDao;

    public UserModel newUser() {
        return new UserModel();
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
        userDao.save(user);
    }

    public void deleteUser(String mail) {
        userDao.deleteById(mail);
    }
}
