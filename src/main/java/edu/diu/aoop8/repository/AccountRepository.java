package edu.diu.aoop8.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.diu.aoop8.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{

}
