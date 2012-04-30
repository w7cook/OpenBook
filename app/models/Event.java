package models;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Event extends Postable implements Comparable<Event>{

  @ManyToOne
  public User owner;

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

  @ManyToMany
  @JoinTable(name="InvitedRepliedEventMembers")
  public Set<User> invited;

  @ManyToMany
  @JoinTable(name="InvitedEventMembers")
  public Set<User> awaitreply;

  @ManyToMany
  @JoinTable(name="EventMembers")
  public Set<User> members;

  @ManyToMany
  @JoinTable(name="MaybeEventMembers")
  public Set<User> maybe;

  @ManyToMany
  @JoinTable(name="DeclinedEventMembers")
  public Set<User> declined;

  public Event(User author, String name, String script, String location) {
    this.owner = author;
    this.name = name;
    this.script = script;
    this.location = location;
    this.invited = new HashSet<User>();
    this.awaitreply = new HashSet<User>();
    this.members = new HashSet<User>();
    this.maybe = new HashSet<User>();
    this.declined = new HashSet<User>();
    this.members.add(owner);
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void inviteMember(User u){
    if (!invited.contains(u)){
      invited.add(u);
      awaitreply.add(u);
    }
  }

  public void addMember(User u) {
    if (!members.contains(u))
      members.add(u);
    if(maybe.contains(u))
      maybe.remove(u);
    if(declined.contains(u))
      declined.remove(u);
    if(awaitreply.contains(u))
      awaitreply.remove(u);
  }

  public void addMaybe(User u) {
    if (!maybe.contains(u))
      maybe.add(u);
    if(members.contains(u))
      members.remove(u);
    if(declined.contains(u))
      declined.remove(u);
    if(awaitreply.contains(u))
      awaitreply.remove(u);
  }

  public void removeMember(User u) {
    if (members.contains(u))
      members.remove(u);
    if(maybe.contains(u))
      maybe.remove(u);
    if(!declined.contains(u))
      declined.add(u);
    if(awaitreply.contains(u))
      awaitreply.remove(u);
  }

  public int getMemberCount() {
    return members.size();
  }

  public int getInvitedCount() {
    return awaitreply.size();
  }

  public int getMaybeCount() {
    return maybe.size();
  }

  public Set<User> uninvitedFriends(User user) {
    HashSet ret = new HashSet(user.friends);
    ret.removeAll(this.invited);//was members
    return ret;
  }

  public int compareTo(Event e){
    if (e.startDate == this.startDate)return 0;
    else if (e.startDate.before(this.startDate)) return 1;
    else return -1;
  }
}