package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;


public class Timeline extends Commentable {
public enum Action {CREATE, MODIFY, DELETE};

@Required
@OneToOne
User author;
Vector<TimelineEvent> events;

public Timeline(User auth){
	this.author = auth;
	events = new Vector<TimelineEvent>();
}

public Vector<TimelineEvent> getEvents(){
	return this.events;
}

}





