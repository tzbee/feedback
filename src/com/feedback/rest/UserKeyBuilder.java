package com.feedback.rest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_KEY")
public class UserKeyBuilder {

	// Temporary value for testing key generation
	private static int TEST_VALUE = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "USER_KEY_VALUE")
	private int keyValue;

	@Column(name = "USER_ACCOUNT_TYPE")
	@Enumerated(EnumType.STRING)
	private UserAccountType accountType;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "USER_PASSWORD")
	private String password;

	public void setAccountType(UserAccountType accountType) {
		this.accountType = accountType;
	}

	public UserAccountType getAccountType() {
		return accountType;
	}

	public int getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	private void generateKeyValue() {
		// TODO
		this.keyValue = TEST_VALUE++;
	}

	private void generatePassword() {
		// TODO
		this.password = "stub";
	}

	public void build() {
		generateKeyValue();
		generatePassword();
	}
}
