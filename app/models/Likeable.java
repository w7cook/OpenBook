package models;

import java.util.*;

import javax.persistence.*;

import controllers.Comments;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public abstract class Likeable extends Model {

  @Required
  @ManyToOne
  public User owner;

  public enum Visibility {PRIVATE, FRIENDS, PUBLIC};

  @Required
  public Visibility visibility;

  @ManyToMany(cascade = CascadeType.PERSIST)
  public Set<User> thoseWhoLike;

  public Likeable() {
    this(Visibility.FRIENDS);
  }

  public Likeable(Visibility v) {
    thoseWhoLike = new HashSet<User>();
    visibility = v;
  }

  public Likeable addLike(User user) {
    thoseWhoLike.add(user);
    this.save();
    return this;
  }

  public Likeable removeLike(User user) {
    thoseWhoLike.remove(user);
    this.save();
    return this;
  }

  public boolean likedBy(User user) {
    return thoseWhoLike.contains(user);
  }

  public int numLikes() {
    return thoseWhoLike.size();
  }

  /*
  @OneToMany(mappedBy="parentObj", cascade=CascadeType.ALL)
  public List<Likes> likes;

  public Likeable() {
    this.likes = new ArrayList<Likes>();
  }

  public Likeable addLike(User user){
    boolean alreadyLikes = Likes.find("author = ? AND parentObj = ?",
                                      user, this).first() != null;
    if (!alreadyLikes) {
      Likes newLike = new Likes(this, user).save();
      this.likes.add(newLike);
      this.save();
    }
    return this;
  }

  public Likeable removeLike(User user){
    Likes relevantLikes = Likes.find("author = ? AND parentObj = ?",
                                       user, this).first();
    if (relevantLikes != null) {
      likes.remove(relevantLikes);
      relevantLikes.delete();
      this.save();
    }
    return this;
  }

  public void addLike (Likes l){
    likes.add(l);
    this.save();
  }

  public void removeLike(Likes l){
    likes.remove(l);
    l.delete();
    this.save();
  }

  public boolean currentUserLiked (){
    User currentUser = Comments.user();
    return Likes.find("author = ? AND parentObj = ?", currentUser,this).first() != null;
  }
  */
}
