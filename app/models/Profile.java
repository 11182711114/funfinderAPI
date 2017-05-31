package models;

import javax.persistence.Entity;
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
	
	private String job;

	public Profile(String uid, String bio, String hobbies) {
//		this.userId = uid;
		this.user = User.find.byId(Long.parseLong(uid));
		this.bio = bio;
		this.hobbies = hobbies;
	}
	
	public Profile() {
	}

	public Profile(User user, String bio, String hobbies) {
		this.user = user;
		this.bio = bio;
		this.hobbies = hobbies;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}
	
	public String getBio() {
		return bio;
	}
	
	public String getHobbies() {
		return hobbies;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return user.getId() + " " + bio + " " + hobbies;
	}

	public Profile copy() {
		return new Profile(user,bio,hobbies);
	}

}
