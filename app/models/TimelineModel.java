package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;

@Entity
public class TimelineModel extends Commentable {
  public enum Action {
    CREATE, MODIFY, DELETE
  };

  Vector<TimelineEvent> events;

  public TimelineModel(User auth) {
    super(auth);
    events = new Vector<TimelineEvent>();
  }

  public Vector<TimelineEvent> getEvents() {
    return this.events;
  }

  public void addEvent(Object o, TimelineModel.Action action,
                       Vector<User> participants, String foreword) {
    this.events.add(new TimelineEvent(this, o, action, participants, foreword));
  }
}