package models;

import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.validation.*;

@Entity
@Table(name = "User")
public class User extends Model{
	
	@Id
	@Column(name = "idUser")
	public Long id;
	
	@Constraints.Required
	@Column(name = "firstName")
	public String firstName;

	@Constraints.Required
	@Column(name = "lastName")
	public String lastName;

	@Column(name = "age")
	public int age;

	@Column(name = "email")
	public String email;
	
	public User(String fName, String lName) {
		this.firstName = fName;
		this.lastName = lName;
	}
	
	public static Finder<Long, User> find = new Finder<Long,User>(User.class);
}
