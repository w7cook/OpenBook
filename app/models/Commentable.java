package models;

import java.util.*;

import javax.persistence.*;
 
import play.db.jpa.*;

@Entity
public class Commentable extends Model
{	

	@OneToMany(mappedBy = "parentObj", cascade = CascadeType.ALL)
	public List<Comment> allComments;
	
	// @OneToMany(mappedBy = "parentObj", cascade = CascadeType.ALL)
	// public List<Like> allLikes;
	
	public Commentable addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content).save();
		this.allComments.add(newComment);
		this.save();
		return this;
	}
	
	/*
	public Commentable addLike( ){
	  Like newLike = new Like( ).save();
	  this.addLike.add(newLike);
	  this.save();
	  return this;
	}
	*/
}
