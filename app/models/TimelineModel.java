package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;

@Entity
public class TimelineModel extends Model {
  public enum Action {
    CREATE, MODIFY, DELETE
  };

  @Required
  @OneToOne
  User author;
  
  @Lob
  Vector<TimelineEvent> events;

  public TimelineModel(User auth) {
    this.author = auth;
    events = new Vector<TimelineEvent>();
  }

  public Vector<TimelineEvent> getEvents() {
    return this.events;
  }

  public void addEvent(long objectId, TimelineModel.Action action,
      Vector<User> participants, String foreword) {
    this.events.add(new TimelineEvent(this, objectId, action, participants, foreword));
  }

}
