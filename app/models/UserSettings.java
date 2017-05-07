package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UserSettings {
	
	@Id
	private int id;
	
	@OneToOne
	private User user;
	
	@Column(name = "user_prefs")
	private String userPrefs;
	
	@Column(name = "push_notices")
	private boolean pushNotices;

	public String getUserPrefs() {
		return userPrefs;
	}

	public void setUserPrefs(String userPrefs) {
		this.userPrefs = userPrefs;
	}
	
}
