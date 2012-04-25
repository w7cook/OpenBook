package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;


public class Timeline extends Commentable {
  public enum Action {CREATE, MODIFY, DELETE};

  Vector<TimelineEvent> events;

  public Timeline(User auth){
    super(auth);
    events = new Vector<TimelineEvent>();
  }

  public Vector<TimelineEvent> getEvents(){
    return this.events;
  }
}





