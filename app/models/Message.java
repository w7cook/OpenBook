package models;

import java.util.*;
import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;

@Entity
public class Message extends Model{

  public String title;
  public Date date;

  @Lob
  public String content;

  @ManyToOne
  public User author;
  
  @ManyToOne
  public User recipient; 
  
  public Message(User author, User recipient, String title, String content) {
    this.author = author;
    this.recipient = recipient;
    this.title = title;
    this.content = content;
    this.date = new Date();
  }
}
