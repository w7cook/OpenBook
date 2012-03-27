package models;

import java.util.*;
import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;
import play.modules.elasticsearch.annotations.ElasticSearchable;


@Entity
public class Post extends Status {
  
  public enum type{NEWS,PAGE,GROUP,EVENT};
  public type postType;

  public String title;

  @Lob
  public String text;

  private static final int TEASER_LENGTH = 150;

  public Post(User author, String title, String content) {
    super(author, content);
    this.title = title;
    this.text = content;
    this.postType = type.NEWS;
  }
  
  public Post(User author, String title, String content, type t) {
    super(author, content);
    this.title = title;
    this.text = content;
    this.postType = t;
  }
  
  public String contentTeaser() {
	  if (this.content.length() < TEASER_LENGTH) {
		  return this.content;
	  } else {
		  return this.content.substring(0, TEASER_LENGTH);
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
