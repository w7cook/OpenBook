package models;

import java.util.*;
import javax.persistence.*;

import controllers.Events;

import play.db.jpa.*;

@Entity
public class EventInvite extends Model {
	
	@ManyToOne
	public Event curEvent;
	@ManyToOne
	public User curGuest;
	
	boolean viewedInvite;
	boolean hasResponded;
	boolean isAttending;
	boolean maybeAttending;
	boolean declinedAttending;
	
	public EventInvite(Event curEvent, User curGuest){
		this.curEvent = curEvent;
		this.curGuest = curGuest;
		hasResponded = false;
		isAttending = false;
		maybeAttending = false;
		declinedAttending = false;
	}
	
	public User getGuest(){
		return this.curGuest;
	}
}