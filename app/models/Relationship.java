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
	
	public boolean accepted;
	public boolean requested;
	
	public Relationship(User from, User to, boolean requested) {
		this.from = from;
		this.to = to;
		this.requested = requested;
		this.accepted = false;
	}
}
