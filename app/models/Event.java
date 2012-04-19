package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Event extends Postable {

  @ManyToOne
  public User author;

  /*
   * @OneToMany public EventInvite invitedUsers;
   */

  public String name;
  public String script;
  public String location;
  public Date startDate;
  public Date endDate;
  public boolean givenEndDate = false;

  public String privilege;
  public boolean open = false;
  public boolean friends = false;
  public boolean inviteOnly = false;
  // public Location eventVenue;

  @ManyToMany
  @JoinTable(name="EventMembers")
  public List<User> members;

  public Event(User author, String name, String script,
      String location) {
    this.author = author;
    this.name = name;
    this.script = script;
    this.location = location;
    this.members = new ArrayList<User>();
    this.members.add(author);
  }

  public EventInvite newEventInvite(User curGuest) {
    EventInvite myEventInvite = new EventInvite(this, curGuest).save();
    this.save();
    return myEventInvite;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void addMember(User u) {
    if (!members.contains(u))
      members.add(u);
  }

  public void removeMember(User u) {
    if (members.contains(u))
      members.remove(u);
  }

  public int getMemberCount() {
    return members.size();
  }
}