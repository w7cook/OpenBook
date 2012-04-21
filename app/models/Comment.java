package models;

import java.util.*;
import javax.persistence.*;

import controllers.Secure;

import controllers.Comments;
import controllers.Secure;
import play.db.jpa.*;

@Entity
public class Comment extends Likeable {

  public boolean approved;

  @Lob
  public String content;

  @ManyToOne
  public Commentable parentObj;

  public Comment(Commentable parentObj, User author, String content) {
    this.parentObj = parentObj;
    this.content = content;
    this.approved = false;
    this.owner = author;
  }
}
