package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Status extends Model {
    
    @Required
    public User author;
    
    @Lob
    @Required
    @MaxSize(1000)
    public String message;
    
    @Required
    public Date update_time;
    
    @Required
    public String type; // Text, Link, Location(Check in), Poll
    
    //@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	//public List<Comment> comments;
    
    // @OneToMany(mappedBy="status", cascade=CascadeType.ALL)
    // public ArrayList<Like> likes;
    
    // @ManyToMany(cascade=CascadeType.PERSIST)
    // public Set<Tag> tags;
    
    public Status(User author, String message) {
        //this.comments = new ArrayList<Comment>();
        // this.likes = new ArrayList<Like>();
        // this.tags = new ArrayList<Tag>();
        this.author = author;
        this.message = message;
        this.update_time = new Date();
    }
    
    /*
    public Status addComment(String author, String content){
        Comment newComment = new Comment(this, author, content).save();
        this.comments.add(newComment);
        this.save();
        return this;
    }
    */
    
    /*
    public Status addLike(...){
        Like newLike = new Like(...).save();
        this.likes.add(newLike);
        this.save();
        return this;
    }
    */
    
    public Status previous() {
        return Post.find("update_time < ? order by update_time asc", update_time).first();
    }
    
    public Status next() {
        return Post.find("update_time > ? order by update_time asc", update_time).first();
    }
    
    /*
    public static List<Status> findTaggedWith(String... tags) {
        return Status.fin(
                "select distiinct p from Status p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.message, p.update_time having count(t.id) = :size"
        ).bind("tags", tags).bind("size", tags.length).fetch();
    }
    */
}
