package models;

import java.util.*;
import javax.persistence.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Status extends Commentable {
/*
* The reason status messages did not replace post is because post can still be
* used as a blog post for a user. The idea is to use status messages as quick
* news message.
*/
  private static final Pattern links_pattern = Pattern.compile("\\b?[@#]\\w*\\b"); // A pattern to pull links (mentions and tags) from content
 
  @Required
  @ManyToOne
  public User author; // The User who authored the status update
  
  @Lob
  @Required
  @MaxSize(1000)
  public String content; // The status update text
  
  public Date date; // The time when submitted
  
  public Status linked_object;  // Think Retweet
  
  @ManyToMany(cascade=CascadeType.PERSIST)
  public Set<Tag> tags;
  
  @ManyToMany
  public List<User> mentions;
  
  public Status(User author) {
    this.author = author;
    this.date = new Date();
  }

  private String parseContent(String unlinked_content){
    // TODO implement a string parser that pulls out @ and # tags
    Matcher links_matcher = links_pattern.matcher(unlinked_content);
    
    while(links_matcher.find() ){
      String match = links_matcher.group();
      if(match.startsWith("#") { // tag
        Tag newTag = 
        this.tags.add(newTag);
        this.save();
      }
      else if(match.startsWith("@") {
        
      }
      else
      // error
    }
    
    return unlinked_content;
  }
  
  /* TODO
  private static someFunction(...message parsing...){
    
  }
  */
  
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

  public Status previous() {
    return Post.find("update_time < ? order by update_time asc", date).first();
  }
  
  public Status next() {
    return Post.find("update_time > ? order by update_time asc", date).first();
  }
  
  /* TODO
  public static List<Status> findTaggedWith(String... tags) {
    return Status.fin(
            "select distiinct p from Status p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.message, p.update_time having count(t.id) = :size"
    ).bind("tags", tags).bind("size", tags.length).fetch();
  }
  */

}
