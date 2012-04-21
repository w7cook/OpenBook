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
public class User extends Postable {

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
  
  @OneToOne
  public TimelineModel timeline;

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
  public List<Relationship> friends; // A list of the user's friendship history

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
  public List<Relationship> friendedBy; // A list of the user's friendship history

  @ElasticSearchIgnore
  public boolean subscription; // Whether the user has allowed subscriptions or not

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
  public List<Subscription> subscribedTo; // A list of subscriptions the user subscribed to

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "subscribed", cascade = CascadeType.ALL)
  public List<Subscription> subscribers; // A list of subscriptions to the user's subscribers

  public User(String email, String password, String username) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    this.save();
    new Relationship(this).save();
    // this.education = new ArrayList<Enrollment>();
  }

  public User(String email, String password, String username, String first_name, String last_name) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    this.first_name = first_name;
    this.last_name = last_name;
    this.name = first_name + " " + last_name;
    
    this.save();
    profile = new Profile(this);
    profile.save();
    new Relationship(this).save();
    this.save();
    this.timeline = new TimelineModel(this);
    timeline.save();
    this.save();
    // this.education = new ArrayList<Enrollment>();
  }
  
  public User(TempUser user) {
    if (user.verified == false) {
      this.email = user.email;
      this.password = user.password;
      this.username = user.username;
      this.first_name = user.first_name;
      this.last_name = user.last_name;
      user.verified = true;
      
      this.save();
      profile = new Profile(this);
      profile.save();
      new Relationship(this).save();
      this.save();
    this.timeline = new TimelineModel(this);
    timeline.save();
    this.save();
    }
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
  
  public int unreadCount() {
   return Message.find("SELECT m FROM Message m WHERE (m.author = ?1 OR m.recipient = ?1) AND m.read = false", this).fetch().size();
  }
  
  public List<Note> viewNotes() {
	    return Message.find("SELECT n FROM Note n WHERE n.author = ?1", this).fetch();
	  }
  


  public List<Comment> comments() {
    return Comment.find("byAuthor", this).fetch();
  }


  public List<Post> news() {
    return Post.find(
                     "SELECT p FROM Post p, IN(p.author.friendedBy) u WHERE (u.from.id = ?1 and p.postedObj.id = u.to.id) and (U.accepted = true or (u.to.id = ?1 and p.postedObj.id = u.from.id)) order by p.updatedAt desc",
                     this.id).fetch();
  }

  public List<Post> subscriptionNews() {
    return Post.find(
                     "SELECT p FROM Post p, IN(p.author.subscribers) u WHERE u.subscriber.id = ?1 and p.postedObj.id = u.subscribed.id order by p.updatedAt desc",
                     this.id).fetch();
  }

  public Profile getProfile(){
    if(profile == null){
      profile = new Profile(this);
      profile.save();
    }
    return profile;
  }

  public void createTimeline() {
    if (this.timeline == null) {
      this.timeline = new TimelineModel(this);
      this.timeline.save();
      this.save();
    }
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

  /** Get a list of <numFriends> users who have requested to be friends
   *
   * @param numFriends the number of friends you want to fetch.
   * @return a list of relationships related to incoming friend requests
   */
  public List<Relationship> requestedFriends(int numFriends) {
    return Relationship.find("SELECT r FROM Relationship r where r.to = ? and r.requested = true and r.accepted = false", this).fetch(numFriends);
  }

  /** Get the number of users users who have requested to be friends
   *
   * @return the number of relationships related to incoming friend requests
   */
  public long requestedFriendCount() {
    return Relationship.count("to = ? and requested = true and accepted = false", this);
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

  /** Get all upcoming events
   *
   * @return a list of upcoming events that User has authored
   */
  public List<Event> upcomingEvents() {
    return Event.find("SELECT r FROM Event r where r.author = ?1 AND r.endDate >= ?2", this, new Date()).fetch();
  }

  /** Get all past events
   *
   * @return a list of past events that User has authored
   */
  public List<Event> pastEvents() {
    return Event.find("SELECT r FROM Event r where r.author = ?1 AND r.endDate < ?2 ", this, new Date()).fetch();
  }
  
  /** List all events for any user
   * 
   * @return a list of events the user is a member of
   */
  public List<Event> myEvents() {
    List<Event> allEvents= Event.findAll();
    List<Event> answer= new ArrayList<Event>();
    for(Event e : allEvents){
      for(User u : e.members){
        if(u.equals(this)){
          answer.add(e);
          break;
        }
      }
    }
    return answer;
  }
  
  /** List all friends uninvited to an event
   * 
   * @return a list of users that are friends with the current user, not yet invited to event
   */
  public List<User> uninvitedFriends(Long eventId, Long userId){
    User guest = User.findById(userId);
    Event event = Event.findById(eventId); 
    List<Relationship> friends = guest.confirmedFriends();
    List<User> inviteFriends = new ArrayList<User>();
    
    for(int i = 0; i < friends.size(); i++){
      User u = friends.get(i).to;
      if (!(event.members).contains(u)){
        inviteFriends.add(u);
      }
    }
    return inviteFriends;
  }
}
