package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Comment extends Model {

  public User author;
  public boolean approved;

  @Lob
  public String content;

  @ManyToOne
  public Status parentObj;

  public Comment(Status parentObj, User author, String content) {
    this.parentObj = parentObj;
    this.author = author;
    this.content = content;
    this.approved = false;
  }
}
