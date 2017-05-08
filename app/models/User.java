package models;


import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model;

import play.data.validation.*;

@Entity
@Table(name = "User")
public class User extends Model{
	
	@Id
	@Column(name = "id")
	public int id;
	
	@Constraints.Required
	@Column(name = "firstName")
	public String firstName;

	@Constraints.Required
	@Column(name = "lastName")
	public String lastName;

	@Column(name = "birthdate")
	public LocalDate birthDate;

	@Column(name = "email")
	public String email;
	
	//FIXME Needs to be secured
	@Column(name = "password")
	private String password;
	
	@OneToOne
	private Location loc;
	
	public User(String fName, String lName) {
		this.firstName = fName;
		this.lastName = lName;
	}
	
	public User(String fName, String lName, String birthdate, String email, String password) {
		this.firstName = fName;
		this.lastName = lName;
		this.birthDate = LocalDate.parse(birthdate);
		this.email = email;
		this.password = password;
		
	}
	
	public static Finder<Long, User> find = new Finder<Long,User>(User.class);
}
