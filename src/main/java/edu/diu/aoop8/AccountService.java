package edu.diu.aoop8;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.diu.aoop8.model.Account;
import edu.diu.aoop8.repository.AccountRepository;

@Service

public class AccountService {
	@Autowired
	AccountRepository repo;

	public List<Account> listAll() {
		return (List<Account>) repo.findAll();
	}

	public Account save(Account account) {
		return repo.save(account);

	}

	public Account get(long id) {
		return repo.findById(id).get();
	}

	public void delete(long id) {
		repo.deleteById(id);
	}

}
