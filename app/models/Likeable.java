package models;

import java.util.*;

import javax.persistence.*;

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

  @Required
  @ManyToMany(cascade = CascadeType.PERSIST)
  public Set<User> thoseWhoLike;

  public Likeable(User owner) {
    this(owner, Visibility.FRIENDS);
  }

  public Likeable(User owner, Visibility v) {
    this.thoseWhoLike = new HashSet<User>();
    this.visibility = v;
    this.owner = owner;
  }

  public Likeable addLike(User user) {
    System.out.println("User: " + user + " is liking this");
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
}
