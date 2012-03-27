package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Likes extends Model {
  
  @Required
  @ManyToOne
  public User author;
  
  @Required
  @ManyToOne
  public Likeable parentObj;
  
  public Likes(Likeable parentObj, User author){
    this.author = author;
    this.parentObj = parentObj;
  }
}
