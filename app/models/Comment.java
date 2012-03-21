package models;

import java.util.*;
import javax.persistence.*;

import controllers.Comments;
import controllers.Secure;

import play.db.jpa.*;

@Entity
public class Comment extends Model {

  @ManyToOne
  public User author;
  public boolean approved;

  @Lob
  public String content;

  @ManyToOne
  public Commentable parentObj;
  
  @OneToMany(mappedBy="parentObj", cascade=CascadeType.ALL)
  public List<Like> likes;

  public Comment(Commentable parentObj, User author, String content) {
    this.parentObj = parentObj;
    this.author = author;
    this.content = content;
    this.approved = false;
  }

  public void addLike (Like l){
    likes.add(l);
    this.save();
  }
  
  public void removeLike(Like l){
    likes.remove(l);
    l.delete();
    this.save();
  }
  
  public boolean currentUserLiked (){
    User currentUser = Comments.user();
    return Likes.find("author = ? AND comment = ?", currentUser,this).first() != null;
  }
  
}
