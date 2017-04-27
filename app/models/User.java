package models;

import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.validation.*;

@Entity
public class User extends Model{

	@Id
	public Long id;
	
	@Constraints.Required
	public String firstName;

	@Constraints.Required
	public String lastName;
	
	public int age;
	
	public String email;
	
	public static Finder<Long, User> find = new Finder<Long,User>(User.class);
}
