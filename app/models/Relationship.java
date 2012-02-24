package models;

import java.util.Date;
import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Relationship extends Model {

  @ManyToOne
  public User from;

  @ManyToOne
  public User to;

  // Both the from/to relationship will have accepted if users are friends
  // Should only need to check friendship of one to prove friendship
  public boolean accepted;

  // If "from" user has requested, and "to" user has not, will be true.
  public boolean requested;

  // Relationships are established when a user makes a friendship request
  public Relationship(User from, User to, boolean requested) {
    this.from = from;
    this.to = to;
    this.requested = requested;
    this.accepted = false;
  }
}
