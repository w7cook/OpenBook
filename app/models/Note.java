package models;

import java.util.*;
import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;
import play.modules.elasticsearch.annotations.ElasticSearchable;

/*
 * Author: Andy Eskridge
 * email: andy.eskridge@gmail.com
 */

@Entity
public class Note extends Status {

  public String title;

  @Lob
  public String text;

  private static final int TEASER_LENGTH = 150;

  public Note(User author, String title, String content) {
    super(author, content);
    this.title = title;
    this.text = content;
  }

  public String contentTeaser() {
    if (this.content.length() < TEASER_LENGTH) {
      return this.content;
    } else {
      return this.content.substring(0, TEASER_LENGTH);
    }
  }

  public Post previous() {
    return Post.find("owner = ? AND date < ? order by date desc",
                     this.owner, this.createdAt).first();
  }

  public Post next() {
    return Post.find("date > ? order by date asc", this.createdAt)
      .first();
  }

  public boolean byCurrentUser() {
    return owner.email.equals( Security.connected() );
  }
}
