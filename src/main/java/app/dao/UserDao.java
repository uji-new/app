package app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.UserModel;

public interface UserDao extends JpaRepository<UserModel, String> {
}
