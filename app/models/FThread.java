package models;

import java.util.*;

import javax.persistence.*;

import play.data.binding.*;
import play.data.validation.*;
import play.db.jpa.Model;

@Entity
public class FThread extends Commentable {

  @Required
  public String title;

  @Required
  @ManyToOne
  public User author;

  @Required @As("yyyy-MM-dd")
  public Date postedAt;

  @Required
  @ManyToOne
  public Category category;

  @Required
  @Lob
  public String content;

  public FThread(String title, User author, Date postedAt, String content) {
    this.title = title;
    this.author = author;
    this.postedAt = new Date();
    this.content = content;
  }

  public List<Comment> comments() {
    return Comment.find("parentObj = ? order by createdAt asc", this).fetch();
  }
}