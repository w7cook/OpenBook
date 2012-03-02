package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class Comment extends Model {
 
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
 
}