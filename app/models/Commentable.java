package models;

import java.util.*;

import javax.persistence.*;
import play.db.jpa.*;

@Entity
public abstract class Commentable extends Likeable
{
  @OneToMany(mappedBy="parentObj", cascade=CascadeType.ALL)
  public List<Comment> comments;

  public Commentable addComment(User author, String content) {
    Comment newComment = new Comment(this, author, content).save();
    this.comments.add(newComment);
    this.save();
    return this;
  }

  // Remvoed add/delete like as that is already present in Likeable.
  public void removeLikes (Comment com, User au){
    Likes toRemove = Likes.find("author = ? AND comment = ?", au, com).first();
    toRemove.delete();
    this.save();   
  }
  

  public ArrayList<Object> getComments(){
	  return (ArrayList<Object>) Comment.find("SELECT c FROM Comment c WHERE c.parentObj.id = ? order by c.updatedAt desc", this.id).fetch();
  }
}
