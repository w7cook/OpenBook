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
  @JoinTable(name="friends_table")
  @ManyToMany(cascade = CascadeType.PERSIST)
  public Set<User> friends;

  @ElasticSearchIgnore
  @JoinTable(name="friend_requests_table")
  @ManyToMany(cascade = CascadeType.PERSIST)
  public Set<User> friendRequests; // A list of the user's friendship history

  @ElasticSearchIgnore
  public boolean subscription; // Whether the user has allowed subscriptions or not

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
  public List<Subscription> subscribedTo; // A list of subscriptions the user subscribed to

  @ElasticSearchIgnore
  @OneToMany(mappedBy = "subscribed", cascade = CascadeType.ALL)
  public List<Subscription> subscribers; // A list of subscriptions to the user's subscribers

  @ElasticSearchIgnore
  @ManyToMany(mappedBy="thoseWhoLike")
  public Set<Likeable> likes;
  
  @ElasticSearchIgnore
  @ManyToMany(mappedBy="usersWhoAnswered")
  public Set<Answer> userAnswers;
  
  @ElasticSearchIgnore
  @OneToMany(mappedBy="owner")
  public Set<Question> questions;

  public User(String email, String password, String username) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    this.friends = new HashSet<User>();
    this.friendRequests = new HashSet<User>();

    friends.add(this);
    this.save();
    // this.education = new ArrayList<Enrollment>();
  }

  public User(String email, String password, String username, String first_name, String last_name) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    this.first_name = first_name;
    this.last_name = last_name;
    this.name = first_name + " " + last_name;

    profile = new Profile(this);
    profile.save();

    this.friends = new HashSet<User>();
    this.friendRequests = new HashSet<User>();
    friends.add(this);

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

      profile = new Profile(this);
      profile.save();

      this.friends = new HashSet<User>();
      this.friendRequests = new HashSet<User>();
      friends.add(this);

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
    return Message.find("byRecipient", this).fetch();
  }

  public List<Message> unreadMessages() {
    return Message.find("byRecipientAndRead", this, false).fetch();
  }

  public long unreadCount() {
    return Message.find("SELECT m FROM Message m WHERE m.recipient = ?1 AND m.read = false", this).fetch().size();
  }

  public List<Note> viewNotes() {
    return Note.find("byOwner", this).fetch();
  }

  public List<Comment> comments() {
    return Comment.find("byOwner", this).fetch();
  }


  public List<Post> news() {
    return Post.find("SELECT p FROM Post p INNER JOIN p.owner.friends u WHERE u = ?1 AND p.postedObj MEMBER OF User ORDER BY p.updatedAt DESC", this).fetch();
  }

  public List<Post> subscriptionNews() {
    return Post.find("SELECT p FROM Post p, IN(p.owner.subscribers) u WHERE u.subscriber.id = ?1 and p.postedObj.id = u.subscribed.id order by p.updatedAt desc",
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


  /** Get the number of users users who have requested to be friends
   *
   * @return the number outstanding friend requests
   */
  public long numFriendRequests() {
    return friendRequests.size();
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
    return friends.contains(user);
  }

  /** Get all authored events
   *
   * @return a list of events that User has authored
   */
  public List<Event> authoredEvents() {
    return Event.find("SELECT r FROM Event r where r.owner = ? ORDER BY r.startDate", this).fetch();
  }

  /** Get all upcoming events
   *
   * @return a list of upcoming events that User has authored
   */
  public List<Event> upcomingEvents() {
    return Event.find("SELECT r FROM Event r where r.owner = ?1 AND r.startDate >= ?2 ORDER BY r.startDate", this, new Date()).fetch();
  }

  /** Get all past events
   *
   * @return a list of past events that User has authored
   */
  public List<Event> pastEvents() {
    return Event.find("SELECT r FROM Event r where r.owner = ?1 AND r.endDate < ?2 ORDER BY r.startDate", this, new Date()).fetch();
  }

  /** List all past events for any user
   *
   * @return a list of past events the user is a member of
   */
  public List<Event> myPastEvents() {
    //past events
    List<Event> allEvents= Event.findAll();
    List<Event> allUpcoming = myUpcomingEvents();
    List<Event> allToday = todayEvents();
    List<Event> answer= new ArrayList<Event>();
    for(Event e : allEvents){
      for(User u : e.invited){
        if(u.equals(this) && !allUpcoming.contains(e) && !allToday.contains(e)){
          // if(u.equals(this)){
          answer.add(e);
          break;
        }
      }
    }
    Collections.sort(answer);
    return answer;
  }

  /** List all events for any user upcoming
   * 
   * @ return a list of events that haven't happened yet the user is a member of
   */
  public List<Event> myUpcomingEvents(){
    List<Event> allEvents = Event.find("SELECT r FROM Event r where r.startDate > ?", new Date()).fetch();
    List<Event> answer= new ArrayList<Event>();
    Calendar cal = Calendar.getInstance();
    for(Event e : allEvents){
      for(User u : e.invited){
        if(u.equals(this) && (e.startDate.getDate() != cal.get(Calendar.DAY_OF_MONTH) && e.startDate.getMonth() != cal.get(Calendar.MONTH))){
          answer.add(e);
          break;
        }
      }
    }
    Collections.sort(answer);
    return answer;
  }

  /** List all events for any user happening today
   * 
   * @ return a list of events that happen today the user is a member of
   */
  public List<Event> todayEvents(){
    List<Event> allEvents = Event.findAll();
    List<Event> answer= new ArrayList<Event>();
    Calendar cal = Calendar.getInstance();
    for(Event e : allEvents){
      for(User u : e.invited){
        if(u.equals(this) && (e.startDate.getDate() == cal.get(Calendar.DAY_OF_MONTH) && e.startDate.getMonth() == cal.get(Calendar.MONTH))){
          answer.add(e);
          break;
        }
      }
    }
    Collections.sort(answer);
    return answer;
  }

  /** List all events for any user upcoming that the user declined
   * 
   * @ return a list of events that haven't happened yet the user declined
   */
  public List<Event> myDeclinedEvents(){
    List<Event> allEvents = Event.find("SELECT r FROM Event r where r.startDate > ?", new Date()).fetch();
    List<Event> answer= new ArrayList<Event>();
    Calendar cal = Calendar.getInstance();
    for(Event e : allEvents){
      for(User u : e.declined){
        if(u.equals(this) && (e.startDate.getDate() != cal.get(Calendar.DAY_OF_MONTH) && e.startDate.getMonth() != cal.get(Calendar.MONTH))){
          answer.add(e);
          break;
        }
      }
    }
    Collections.sort(answer);
    return answer;
  }

  /** List all declined events for any user happening today
   * 
   * @ return a list of declined events that happen today 
   */
  public List<Event> declinedTodayEvents(){
    List<Event> allEvents = Event.findAll();
    List<Event> answer= new ArrayList<Event>();
    Calendar cal = Calendar.getInstance();
    for(Event e : allEvents){
      for(User u : e.declined){
        if(u.equals(this) && (e.startDate.getDate() == cal.get(Calendar.DAY_OF_MONTH) && e.startDate.getMonth() == cal.get(Calendar.MONTH))){
          answer.add(e);
          break;
        }
      }
    }
    Collections.sort(answer);
    return answer;
  }
}