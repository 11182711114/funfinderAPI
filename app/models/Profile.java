package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "Profile")
public class Profile {
	
//
//	@Id
//	@Column(name = "user")
//	private int userId;

	@JsonInclude
	@OneToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;
	
	@JsonInclude
	private String bio;
	
	@JsonInclude
	private String hobbies;

	public Profile(String uid, String bio, String hobbies) {
//		this.userId = uid;
		this.user = User.find.byId(Long.parseLong(uid));
		this.bio = bio;
		this.hobbies = hobbies;
	}
	
	public String getBio() {
		return bio;
	}
	
	public String getHobbies() {
		return hobbies;
	}

	@Override
	public String toString() {
		return user.id + " " + bio + " " + hobbies;
	}
}
