package models;
 
import java.util.*;
import javax.persistence.*;

import controllers.Secure;
 
import play.db.jpa.*;
 
@Entity
public class Comment extends Commentable {
 
    public String author;
    public Date date;
    public boolean approved;
   
    @Lob
    public String content;
    
    @ManyToOne
    public Commentable parentObj;
 
    public Comment(Commentable parentObj, String author, String content) {
      this.parentObj = parentObj;
      this.author = author;
      this.content = content;
      this.date = new Date();
    }
    
    public boolean currentUserLiked (){
      String name = Secure.Security.connected();
      User currentUser = User.find("email = ?", name).first();
      return Likes.find("author = ? AND comment = ?", currentUser,this).first() != null;
    }
    
}