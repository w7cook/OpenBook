package models;

import java.util.*;
import javax.persistence.*;

import controllers.Secure;

import play.db.jpa.*;

@Entity
public class Post extends Model {

	public String title;
	public Date date;

	@Lob
	public String content;

	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> allComments;

	public List<Comment> comments() {
		return Comment.find("post = ? AND approved=FALSE", this).fetch();
	}

	public Post(User author, String title, String content) {
		this.allComments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.date = new Date();
	}

	public Post addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content).save();
		this.allComments.add(newComment);
		this.save();
		return this;
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
		return author.email.equals( Secure.Security.connected() );
	}
		
}