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
	
	
}
