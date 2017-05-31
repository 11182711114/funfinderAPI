package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Model.Finder;

@Entity
@Table(name = "facebook_login")
public class FacebookLoginInfo {

	
	@Id
	@Column(name = "access_token")
	private String token;
	
	@Column(name = "user")
	private User user;

	public FacebookLoginInfo(String accessToken) {
		this.token = accessToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public static Finder<String, FacebookLoginInfo> find = new Finder<String,FacebookLoginInfo>(FacebookLoginInfo.class);
}
