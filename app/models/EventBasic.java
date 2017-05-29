package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class EventBasic {

	@OneToOne
	Event event;
	
	public Event getEvent() {
		return event;
	}
}
