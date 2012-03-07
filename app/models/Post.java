package models;

import java.util.*;
import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;

@Entity
public class Post extends Commentable {

  public String title;
  public Date date;

  @Lob
  public String content;

  @ManyToOne
  public User author;

  private static final int teaserLength = 40;
  
  public List<Comment> comments() {
    return Comment.find("parentObj = ? AND approved=FALSE", this).fetch();
  }

  public Post(User author, String title, String content) {
    this.allComments = new ArrayList<Comment>();
    this.author = author;
    this.title = title;
    this.content = content;
    this.date = new Date();
  }
  
  public String contentTeaser() {
	  if (this.content.length() < teaserLength) {
		  return this.content;
	  } else {
		  return this.content.substring(0, teaserLength);
	  }
  }

  public Post previous() {
    return Post.find("author = ? AND date < ? order by date desc",
                     this.author, this.date).first();
  }

  public Post next() {
    return Post.find("date > ? order by date asc", this.date)
      .first();
  }

  public long numComments() {
    return Post.find("Count(*)").first();
  }

  public boolean byCurrentUser() {
    return author.email.equals( Security.connected() );
  }
}