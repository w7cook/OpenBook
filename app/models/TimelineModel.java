package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;

@Entity
public class TimelineModel extends Model{
  public enum Action {
    CREATE, MODIFY, DELETE
  };

@Required 
@Lob
 private Vector<Post> posts;

  public TimelineModel(User auth) {
    this.posts = new Vector<Post>();
  }

  public Vector<Post> getEvents() {
    return this.posts;
  }

    public void addEvent(long userid, Object o, TimelineModel.Action action, Vector<User> participants, String description) {
      this.posts.add(new Post((User)User.findById(userid), (User)User.findById(userid), description));
      this.save();
  }
}