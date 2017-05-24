package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class UserBasic {

	@OneToOne
	User user;
	
	public User getUser() {
		return user;
	}
}
