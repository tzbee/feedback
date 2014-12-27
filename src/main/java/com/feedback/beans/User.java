package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.owlike.genson.annotation.JsonIgnore;

/**
 * POJO class for a user account
 */
@Entity
@Table(name = "FBS_USER")
public class User {
	@Id
	@Column(name = "USER_NAME", unique = true)
	private String userName;

	@Column(name = "ACCOUNT_TYPE")
	@Enumerated(EnumType.STRING)
	private UserAccountType accountType;

	@JsonIgnore
	@Column(name = "PASSWORD")
	private String password;

	public UserAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(UserAccountType accountType) {
		this.accountType = accountType;
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

	public void setPassword(String password) {
		this.password = password;
	}
}
