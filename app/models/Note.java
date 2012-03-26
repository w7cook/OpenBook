package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import controllers.Security;

import java.util.*;

@Entity
public class Note extends Commentable {
	
	public String title;
	public Date date;

	@Lob
	public String content;

	@ManyToOne
	public User author;



	public List<Comment> comments() {
		return Comment.find("parentObj = ? AND approved=FALSE", this).fetch();
	}

	public Note(User author, String title, String content) {
		this.allComments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.date = new Date();
	}


	public Post previous() {
		return Post.find("author = ? AND date < ? order by date desc",
				this.author, this.date).first();
	}

	public Post next() {
		return Post.find("date > ? order by date asc", this.date)
				.first();
	}

	public long numComments() {
		return Post.find("Count(*)").first();
	}
	
	public boolean byCurrentUser() {
		return author.email.equals( Security.connected() );
	}
    
}
