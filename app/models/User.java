package models;


import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	@Column(name = "firstname")
	public String firstname;

	@Constraints.Required
	@Column(name = "lastname")
	public String lastname;

	@Column(name = "birthdate")
	public LocalDate birthDate;

	@Column(name = "email")
	public String email;
	
	//FIXME Needs to be secured
	@Column(name = "password")
	private String password;
	
	@OneToOne
	private Location loc;
	
	@OneToMany(mappedBy = "from")
	private List<Message> messagesSent;
	
	@OneToMany(mappedBy = "to")
	private List<Message> messagesReceived;
	
	public User(String fname, String lname) {
		this.firstname = fname;
		this.lastname = lname;
	}
	
	public User(String fname, String lname, String birthdate, String email, String password) {
		this.firstname = fname;
		this.lastname = lname;
		this.birthDate = LocalDate.parse(birthdate);
		this.email = email;
		this.password = password;
		
	}
	
	public static Finder<Long, User> find = new Finder<Long,User>(User.class);
}
