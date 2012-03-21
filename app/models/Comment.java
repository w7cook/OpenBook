package models;

import java.util.*;
import javax.persistence.*;

import controllers.Secure;

import play.db.jpa.*;

@Entity
public class Comment extends Model {

  public String author;
  public Date date;
 
  @Lob
  public String content;

  @ManyToOne
  public Commentable parentObj;
  
  @OneToMany(mappedBy="comment", cascade=CascadeType.ALL)
  public List<Likes> allLikes;

  public Comment(Commentable parentObj, String author, String content) {
    this.parentObj = parentObj;
    this.author = author;
    this.content = content;
    this.date = new Date();
  }
  
  public void addLike (Likes l){
    allLikes.add(l);
    this.save();
  }
  
  public void removeLike(Likes l){
    allLikes.remove(l);
    l.delete();
    this.save();
  }
  
  public boolean currentUserLiked (){
    String name = Secure.Security.connected();
    User currentUser = User.find("email = ?", name).first();
    return Likes.find("author = ? AND comment = ?", currentUser,this).first() != null;
  }
  
}