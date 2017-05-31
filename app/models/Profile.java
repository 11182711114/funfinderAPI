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
	
	@JsonInclude
	private String job;

	public Profile(String uid, String bio, String hobbies) {
//		this.userId = uid;
		this.user = User.find.byId(Long.parseLong(uid));
		this.bio = bio;
		this.hobbies = hobbies;
	}
	
	public Profile() {
	}

	public Profile(User user, String bio, String hobbies, String job) {
		this.user = user;
		this.bio = bio;
		this.hobbies = hobbies;
		this.job = job;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return user.getId() + " " + bio + " " + hobbies + " " + job;
	}

	public Profile copy() {
		return new Profile(user,bio,hobbies,job);
	}

}
