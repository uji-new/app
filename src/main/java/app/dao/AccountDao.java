package app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.AccountModel;

public interface AccountDao extends JpaRepository<AccountModel, String> {
}
