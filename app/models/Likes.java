package models;

import java.util.*;
import javax.persistence.*;

import controllers.Secure;
import play.db.jpa.*;
 
@Entity
public class Likes extends Model{
  
  @ManyToOne
  private User author;
  
  @ManyToOne
  private Comment comment;
  
  public Likes (Comment c, User a){
    this.author = a;
    this.comment = c;
  }

}