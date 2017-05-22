package models;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
@Table(name ="BookedEvent")
public class BookedEvent extends Model{
	
	@Id
	private int id;
	
	@OneToOne
	@JoinColumn(name= "user1", referencedColumnName="id")
	private User user1;
	
	@OneToOne
	@JoinColumn(name= "user2", referencedColumnName="id")
	private User user2;
	
	@OneToOne
	@JoinColumn(name= "restaurant", referencedColumnName="id")
	private Restaurant restaurant;
	
	private LocalDate date;
	
	private LocalTime time;
	
	
	
	public BookedEvent(User us1, User us2, LocalDate date, LocalTime time, Restaurant rest){
		this.user1 = us1;
		this.user2 = us2;
		this.date = date;
		this.time = time;
		this.restaurant = rest;
	}


	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}



	public static Finder<Long, BookedEvent> find = new Finder<Long, BookedEvent>(BookedEvent.class);
	
}
