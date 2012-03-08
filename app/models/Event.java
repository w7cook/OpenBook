package models;

import java.util.*;
import javax.persistence.*;
import play.data.validation.*;

import controllers.Events;

import play.db.jpa.*;

@Entity
public class Event extends Model {

	@ManyToOne
	public User author;
	
	/*
	@OneToMany
	public EventInvite invitedUsers;
	*/
	
	public String eventName;
	public String eventScript;
	public String eventLocation;
	public Date startDate;
	public Date endDate;
	
	public String privilege; 
	public boolean open = false;
	public boolean friends = false;
	public boolean inviteOnly = false;
	//public Location eventVenue;
	
	public Event(User author, String eventName, String eventScript, String eventLocation){		
		this.author = author;
		this.eventName = eventName;
		this.eventScript = eventScript;
		this.eventLocation = eventLocation;
	}
	
	public EventInvite newEventInvite(User curGuest) {
		EventInvite myEventInvite = new EventInvite(this, curGuest).save();
		this.save();
		return myEventInvite;
	}	
}