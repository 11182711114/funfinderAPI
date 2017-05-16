package models;


import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model;

import play.data.validation.*;

@Entity
@Table(name = "User")
public class User extends Model{
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Constraints.Required
	@Column(name = "firstname")
	private String firstname;

	@Constraints.Required
	@Column(name = "lastname")
	private String lastname;

	@Column(name = "birthdate")
	private LocalDate birthdate;

	@Column(name = "email")
	private String email;
	
	//FIXME Needs to be secured
	@Column(name = "password")
	private String password;
	
	@Column(name = "created")
	private LocalDateTime created;
	
	@Column(name = "updated")
	private LocalDateTime updated;
	
	@OneToOne
	@JoinColumn(name = "loc_id")
	private Location loc;
	
//	@OneToMany(mappedBy = "sender")
//	private List<Message> messagesSent;
//	
//	@OneToMany(mappedBy = "receiver")
//	private List<Message> messagesReceived;
	
	public User(String fname, String lname) {
		this.firstname = fname;
		this.lastname = lname;
	}
	
	public User(String fname, String lname, String birthdate, String email, String password) {
		this.firstname = fname;
		this.lastname = lname;
		this.birthdate = LocalDate.parse(birthdate);
		this.email = email;
		this.password = password;
		
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	 
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}
	
	public static Finder<Long, User> find = new Finder<Long,User>(User.class);

	public int getId() {
		return id;
	}
}
