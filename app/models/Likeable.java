package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public abstract class Likeable extends Model {
  
  @OneToMany(mappedBy="parentObj", cascade=CascadeType.ALL)
  public List<Like> likes;
  
  public Likeable addLike(User user){
	  Like newLike = new Like(this, user).save();
	  this.likes.add(newLike);
	  this.save();
	  return this;
	}
}
