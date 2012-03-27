package models;

import java.util.*;
import javax.persistence.*;

import org.elasticsearch.index.query.QueryBuilders;
import controllers.Application;
import controllers.Messages;
import controllers.Skins;
import controllers.Users;
import play.db.jpa.*;
import play.modules.elasticsearch.*;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.modules.elasticsearch.search.SearchResults;
import play.libs.Crypto;

@ElasticSearchable
@Entity
public class User extends Model {

  public String name; // The user's full name
  public String first_name; // The user's first name
  public String middle_name; // The user's middle name
  public String last_name; // The user's last name
 
  // Code)

  public String username; // The user's username
  
  @ElasticSearchIgnore
  public double timezone; // The user's timezone offset from UTC
  
  @ElasticSearchIgnore
  public Date updated_time; // The last time the user's profile was updated;
  // changes to the
  // languages, link, timezone, verified,
  // interested_in, favorite_athletes, favorite_teams,
  // andvideo_upload_limits are not not reflected in
  // this value


  public boolean verified; // The user's account verification status,
  // either true or false(see below)

  public String email; // The proxied or contact email address granted by the
  // user

  @ManyToOne
  public Skin skin;//Skin (StyleSheet) used by this User
  
  public String password;

  //  User's basic profile information
  @OneToOne
  public Profile profile;

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
  public List<Relationship> friends; // A list of the user's friendship history

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
  public List<Relationship> friendedBy; // A list of the user's friendship history

  public User(String email, String password, String username) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    Skins.setSkin(this.profile,"DEFAULT");//set skin as default skin
    // this.education = new ArrayList<Enrollment>();
  }

  public static User connect(String login, String password) {
    return find("SELECT u FROM User u WHERE (u.email = ?1 OR u.username = ?1) and u.password = ?2", login, Crypto.passwordHash(password)).first();
  }

  public static User getUser(String login) {
    return find("SELECT u FROM User u WHERE u.email = ?1 OR u.username = ?1", login).first();
  }

  public List<Message> inbox() {
    return Message.find("SELECT m FROM Message m WHERE m.author = ?1 OR m.recipient = ?1", this).fetch();
  }
  
  public List<Post> news() {
    return Post.find(
                     "SELECT p FROM Post p, IN(p.author.friendedBy) u WHERE u.from.id = ?1 and (U.accepted = true or u.to.id = ?1) and p.postType = ?2 order by p.updatedAt desc",
                     this.id, Post.type.NEWS).fetch();
  }

  public Profile getProfile(){
    if(profile == null){
      profile = new Profile(this);
      profile.save();
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
    Relationship r1 = Relationship.find("SELECT r FROM Relationship r where r.from = ?1 AND r.to = ?2", current, this).first();
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
  
  public List<Group> getGroups(){
	  List<Group> allGroups= Group.findAll();
	  List<Group> answer= new ArrayList<Group>();
	  for(Group g : allGroups){
		  for(User u : g.members){
			  if(u.equals(this)){
				  answer.add(g);
				  break;
			  }
		  }
	  }
	  return answer;
  }

  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (obj.getClass() != getClass())
      return false;
    return username.equals(((User) obj).username);
  }
  
  public List getPages(){
		return Page.find("SELECT p FROM Page p WHERE p.admin = ?", this).fetch();
	}
	
  public String toString(){
    return first_name + " " + last_name;
  }
  
  public boolean isFriendsWith(User user) {
  	for(Relationship f: this.confirmedFriends()){
  		if(f.to == this && f.from == user)
  			return true;
  		if(f.to == user && f.from == this)
  			return true;
			}
		return false;
	}

  /** Get all authored events
   *
   * @return a list of events that User has authored
   */
  public List<Event> authoredEvents() {
    return Event.find("SELECT r FROM Event r where r.author = ?", this).fetch();
  }
}
