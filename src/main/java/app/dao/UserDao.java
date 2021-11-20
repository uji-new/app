package app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.AccountModel;

public interface UserDao extends JpaRepository<AccountModel, String> {
}
