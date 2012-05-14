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
  @JoinTable(name="whitelist_table")
  @ManyToMany()
  public Set<User> whitelist;

  @Required
  @JoinTable(name="likes_table")
  @ManyToMany(cascade = CascadeType.PERSIST)
  public Set<User> thoseWhoLike;

  public Likeable(User owner) {
    this(owner, Visibility.FRIENDS);
  }

  public Likeable(User owner, Visibility v) {
    this.thoseWhoLike = new HashSet<User>();
    this.whitelist = new HashSet<User>();
    this.visibility = v;
    this.owner = owner;
    this.whitelist.add(owner);
  }

  public boolean addLike(User user) {
    thoseWhoLike.add(user);
    boolean changed = user.likes.add(this);
    this.save();
    return changed;
  }

  public boolean removeLike(User user) {
    thoseWhoLike.remove(user);
    boolean changed = user.likes.remove(this);
    this.save();
    return changed;
  }

  public boolean likedBy(User user) {
    return thoseWhoLike.contains(user);
  }

  public int numLikes() {
    return thoseWhoLike.size();
  }

  public boolean visible(User user) {
    if(visibility == Visibility.PRIVATE)
      return user.equals(owner);
    if(visibility == Visibility.FRIENDS)
      return user.equals(owner) || user.isFriendsWith(owner);
    return true;
  }
}
