package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

import play.db.jpa.*;

@Entity
public class User extends Model {

  public String name; // The user's full name
  public String first_name; // The user's first name
  public String middle_name; // The user's middle name
  public String last_name; // The user's last name

  // Code)

  public String username; // The user's username
  public float timezone; // The user's timezone offset from UTC


  public boolean verified; // The user's account verification status,
  // either true or false(see below)


  public String email; // The proxied or contact email address granted by the
  // user



  @ManyToOne
  public Location location; // The user's current city
  
  public String password;
  
  //  User's basic profile information
  @OneToOne
  public Profile profile;
  boolean profileCreated = false;

  @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
  public List<Relationship> friends; // A list of the user's work history

  @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
  public List<Relationship> friendedBy;

  public User(String email, String password, String username) {
    this.email = email;
    this.password = password;
    this.username = username;
  }

  public static User connect(String email, String password) {
    return find("byEmailAndPassword", email, password).first();
  }

  public List<Post> news() {
    return Post.find(
        "SELECT p FROM Post p, IN(p.author.friendedBy) u WHERE u.from.id = ? and (U.accepted = true or u.to.id = ?)",
        this.id, this.id).fetch();
  }
  
  public Profile getProfile(){
    if(!profileCreated){
      profile = new Profile(this);
      profileCreated = true;
    }
    return profile;
  }

  /** Checks the status of a friendship
   * 
   * @param id the user to check friendship status with
   * @return a string representing the status
   */
  public String checkFriendship(Long id) {
    User current = Application.user();
    if (Application.user().id == id) {
      return "";
    }
    Relationship r1 = Relationship.find("SELECT r FROM Relationship r where r.from = ? AND r.to = ?", current, this).first();
    Relationship r2 = Relationship.find("SELECT r FROM Relationship r where r.to = ? AND r.from = ?", current, this).first();
    if (r1 != null) {
      if (r1.accepted) {
        return "Friends";
      }
      if (r1.requested){
        return "Friendship Requested";
      }
    }
    return "Request Friendship";
  }

  /** Get any confirmed friends
   * 
   * @return a list of relationships for confirmed friends
   */
  public List<Relationship> confirmedFriends() {
    return Relationship.find("SELECT r FROM Relationship r where r.from = ? and r.accepted = true", this).fetch();
  }


  /** Get a list of any users who have requested to be friends
   * 
   * @return a list of relationships related to incoming friend requests
   */
  public List<Relationship> requestedFriends() {
    return Relationship.find("SELECT r FROM Relationship r where r.to = ? and r.requested = true and r.accepted = false", this).fetch();
  }

}
