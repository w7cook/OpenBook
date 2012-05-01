package models;

import java.util.*;

import javax.persistence.*;

import play.data.binding.*;
import play.data.validation.*;
import play.db.jpa.Model;

@Entity
public class FThread extends Commentable
{

	@Required
	public String title;

	@Required
	@As("yyyy-MM-dd")
	public Date postedAt;

	@Required
	@ManyToOne
	public Category category;

	@Required
	@Lob
	public String content;

	public FThread(Category category, String title, User author, Date postedAt,
	        String content)
	{
		super(author);
		this.title = title;
		this.postedAt = new Date();
		this.content = content;
		this.category = category;
		this.category.latest = this;
	}

	public List<Comment> comments()
	{
		return Comment.find("parentObj = ? order by createdAt asc", this)
		        .fetch();
	}

	public Comment last_comment()
	{
		Comment last = null;
		List<Comment> comments = Comment.find(
		        "parentObj = ? order by createdAt asc", this).fetch();
		for (Comment elem : comments)
		{
			last = elem;
		}
		return last;
	}

}