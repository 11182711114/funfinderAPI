package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Event")
public class Event extends Model {

    @Id
    @Column(name = "eventId")
    public int eventId;

    @Constraints.Required
    @Column(name = "date")
    public String date;

    @Constraints.Required
    @Column(name = "time")
    public String time;

    public Event(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public static Finder<Long, Event> find = new Finder<Long, Event>(Event.class);
}



