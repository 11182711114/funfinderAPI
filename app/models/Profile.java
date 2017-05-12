package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Profile")
public class Profile {
	
//
//	@Id
//	@Column(name = "user")
//	private int userId;
	
	@OneToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;
	
	private String bio;
	
	private String hobbies;

	public Profile(String uid, String bio, String hobbies) {
//		this.userId = uid;
		this.user = User.find.byId(Long.parseLong(uid));
		this.bio = bio;
		this.hobbies = hobbies;
	}

}
