package models;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Message extends Commentable {

  @Lob
  public String content;

  @ManyToOne
  public User recipient;

  public boolean read;

  public Message(User author, User recipient, String content) {
    super(author);
    this.recipient = recipient;
    this.content = content;
    this.comments = new ArrayList<Comment>();
    read = false;
  }
  public Comment getRecent() {
    if (this.comments.isEmpty())
      return null;
    return this.comments.get(this.comments.size() - 1);
  }

  @Override
  public Commentable addComment(User owner, String content) {
    read = false;
    return super.addComment(owner, content);
  }

}
