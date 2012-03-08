package models;

import java.util.*;
import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;

@Entity
public class Post extends Status {

  public String title;

  @Lob
<<<<<<< HEAD
  public String text;
=======
  public String content;

  @ManyToOne
  public User author;

  private static final int teaserLength = 40;
  
  public List<Comment> comments() {
    return Comment.find("parentObj = ? AND approved=FALSE", this).fetch();
  }
>>>>>>> 6f424939a333a8eaa91a4856eb56e44664458d9d

  public Post(User author, String title, String content) {
    super(author, content);
    this.title = title;
    this.text = content;
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
                     this.author, this.createdAt).first();
  }

  public Post next() {
    return Post.find("date > ? order by date asc", this.createdAt)
      .first();
  }

  public boolean byCurrentUser() {
    return author.email.equals( Security.connected() );
  }
}
