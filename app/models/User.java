package models;


import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "User")
public class User extends Model{
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "birthdate")
	private LocalDate birthdate;

	@JsonInclude
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
	@JoinColumn(name = "loc")
	private Location loc;

	@OneToMany(mappedBy = "user")
	@JsonBackReference
	private List<Event> events;
	
	@OneToOne(mappedBy = "user1")
	@JsonBackReference
	private BookedEvent bookedEvent1;
	
	@OneToOne(mappedBy = "user2")
	@JsonBackReference
	private BookedEvent bookedEvent2;

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
	
	public String getEmail() {
		return email;
	}
	
	@JsonIgnore
	public BookedEvent getBookedEvent() {
		return bookedEvent1 != null ?  bookedEvent1 : bookedEvent2;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
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
