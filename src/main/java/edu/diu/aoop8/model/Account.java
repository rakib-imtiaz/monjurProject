package edu.diu.aoop8.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {

	private long accountID;
	private String accountHolderName;
	private String accountNumber;
	private String email;
	private String password;

	private double balance;

	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(long accountID, String accountHolderName, String accountNumber, String email, String password,
			double balance) {
		super();
		this.accountID = 100;
		this.accountHolderName = accountHolderName;
		this.accountNumber = accountNumber;
		this.email = email;
		this.password = password;
		this.balance = balance;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getAccountID() {
		return accountID;
	}

	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account [accountID=" + accountID + ", accountHolderName=" + accountHolderName + ", accountNumber="
				+ accountNumber + ", email=" + email + ", password=" + password + ", balance=" + balance + "]";
	}

}
