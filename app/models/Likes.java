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
  
  public Likes ( User a, Comment c){
    this.author = a;
    this.comment = c;
  }

}
