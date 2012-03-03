package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Status extends Model {
/*
* The reason status messages did not replace post is because post can still be
* used as a blog post for a user. The idea is to use status messages as quick
* news message.
*/


  @Required
  @ManyToOne
  public User author; // The User who authored the status update

  @Lob
  @Required
  @MaxSize(1000)
  public String content; // The status update text

  private String unlinked_content;

  public Date date; // The time when submitted

  // ############## TO BE IMPLEMENTED #######################################

  // public Status linked_status;  // Think Retweet

  // public Post linked_post;

  // @Required
  // public String type; // Text, Link, Location(Check in), Poll

  // @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
  // public List<Comment> comments;

  // @OneToMany(mappedBy="status", cascade=CascadeType.ALL)
  // public List<Like> likes;

  // @ManyToMany(cascade=CascadeType.PERSIST)
  // public Set<Tag> tags;

  // @ManyToMany(cascade=CascadeType.PERSIST)
  //public List<User> mentions;

  // ########################################################################

  public Status(User author, String content) {
    // this.comments = new ArrayList<Comment>();
    // this.likes = new ArrayList<Like>();
    // this.tags = new ArrayList<Tag>();
    this.author = author;
    this.unlinked_content = content;
    this.content = parseContent(unlinked_content);
    this.date = new Date();
    // this.mentions = someFunction(...message parsing...);
    // this.linked_post = someFunction(...message parsing...);
    // this.linked_status = someFuntion(...message parsing...);
  }

  private String parseContent(String unlinked_content){
    // TODO implement a string parser that pulls out @ and # tags
    return unlinked_content;
  }

  /* TODO
  private static someFunction(...message parsing...){

  }
  */

  /* TODO
  public Status addComment(String author, String content){
    Comment newComment = new Comment(this, author, content).save();
    this.comments.add(newComment);
    this.save();
    return this;
  }
  */

  /* TODO
  public Status addLike(...){
    Like newLike = new Like(...).save();
    this.likes.add(newLike);
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
  public List<Comment> comments() {
    return comments;
  }
  */

  /* TODO
  public static List<Status> findTaggedWith(String... tags) {
    return Status.fin(
            "select distiinct p from Status p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.message, p.update_time having count(t.id) = :size"
    ).bind("tags", tags).bind("size", tags.length).fetch();
  }
  */
}