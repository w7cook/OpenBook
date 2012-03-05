package models;

import java.util.*;

import javax.persistence.*;
 
import play.db.jpa.*;

@Entity
public class Commentable extends Model
{	

	@OneToMany(mappedBy = "parentObj", cascade = CascadeType.ALL)
	public List<Comment> allComments;
	
  @OneToMany(mappedBy="comment", cascade=CascadeType.ALL)
  public List<Likes> allLikes;
  
	public Commentable addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content).save();
		this.allComments.add(newComment);
		this.save();
		return this;
	}
	
  public void addLikes (Comment com, User au){
    Likes newOne = new Likes (au, com).save();
    allLikes.add(newOne);
    this.save();
  }
  
  public void removeLikes (Comment com, User au){
    Likes toRemove = Likes.find("author = ? AND comment = ?", au, com).first();
    toRemove.delete();
    this.save();   
  }
}
