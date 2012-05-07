package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

import play.utils.HTML;

@Entity
public class Comment extends Likeable {
  public boolean approved;

  @Lob
  public String content;

  @ManyToOne
  public Commentable parentObj;

  public Comment(Commentable parentObj, User author, String content) {
    super(author);
    this.parentObj = parentObj;
    this.content = HTML.htmlEscape(content);
    this.approved = false;
  }
}