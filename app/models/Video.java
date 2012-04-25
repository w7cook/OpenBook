package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public abstract class Video extends Commentable {
  
  @Required
  public String title;

  public String caption;

  @Required
  @ManyToOne
  public User owner;
  
  public List<Comment> comments() {
    return Comment.find("parentObj = ? order by createdAt asc", this).fetch();
  }
  
  
  
}
