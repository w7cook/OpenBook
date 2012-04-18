package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Message extends Commentable {

  @Lob
  public String content;

  @ManyToOne
  public User author;
  
  @ManyToOne
  public User recipient; 
  
  public Message(User author, User recipient, String content) {
    this.author = author;
    this.recipient = recipient;
    this.content = content;
  }
  public Comment getRecent() {
    if (this.comments.isEmpty())
      return null;
    return this.comments.get(this.comments.size() - 1);
  }
}
