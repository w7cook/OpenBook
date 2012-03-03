package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;
import controllers.Skins;
import play.db.jpa.*;
import play.libs.Crypto;

@Entity
public class User extends Model {

  public String name; // The user's full name
  public String first_name; // The user's first name
  public String middle_name; // The user's middle name
  public String last_name; // The user's last name
  public String gender; // The user's gender: female or male
  public String locale; // The user's locale (ISO Language Code and ISO Country
  // Code)

  public String username; // The user's username
  public float timezone; // The user's timezone offset from UTC
  public Date updated_time; // The last time the user's profile was updated;
  // changes to the
  // languages, link, timezone, verified,
  // interested_in, favorite_athletes, favorite_teams,
  // andvideo_upload_limits are not not reflected in
  // this value

  public boolean verified; // The user's account verification status,
  // either true or false(see below)
  public String bio; // The user's biography
  public Date birthday; // The user's birthday
  // @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  // public List<Enrollment> education; // A list of the user's education
  // history
  public String email; // The proxied or contact email address granted by the
  // user

  @OneToOne
  public User significant_other; // The user's significant other
  public Date anniversary; // date of anniversary
  public String website; // The URL of the user's personal website

  // not implemented yet!!
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  public List<UserLanguage> languages; // The user's languages
  @ManyToOne
  public Location hometown; // The user's hometown
  // public List<String> interested_in; // The genders the user is interested in
  @ManyToOne
  public Location location; // The user's current city

  public String political; // The user's political view
  public String quotes; // The user's favorite quotes
  public String relationship_status; // The user's relationship
  // status:Single, In a
  // relationship, Engaged,Married, It's
  // complicated, In an open
  // relationship, Widowed,Separated, Divorced, In
  // a civil union, In a domestic
  // partnership
  public String religion; // The user's religion

  @ManyToOne
  public Skin skin;//Skin (StyleSheet) used by this User

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  public List<Employment> work; // A list of the user's work history

  public String password;

  @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
  public List<Relationship> friends; // A list of the user's work history

  @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
  public List<Relationship> friendedBy; // A list of the user's work history

  public User(String email, String password, String username) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    Skins.setSkin(this,"DEFAULT");//set skin as default skin
    // this.education = new ArrayList<Enrollment>();
  }

  public static User connect(String login, String password) {
    return find("SELECT u FROM User u WHERE (u.email = ?1 OR u.username = ?1) and u.password = ?2", login, Crypto.passwordHash(password)).first();
  }

  public static User getUser(String login) {
    return find("SELECT u FROM User u WHERE u.email = ?1 OR u.username = ?1", login).first();
  }

  public List<Post> news() {
    return Post.find(
                     "SELECT p FROM Post p, IN(p.author.friendedBy) u WHERE u.from.id = ? and (U.accepted = true or u.to.id = ?)",
                     this.id, this.id).fetch();
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
