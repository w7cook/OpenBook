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
	
	public String startMonth;
	public String startDay;
	public String startTime;
	
	public String endMonth;
	public String endDay;
	public String endTime;
	
	public String eventName;
	public String eventScript;
	public Date startDate;
	public Date endDate;
	
	public String eventLocation;
	public String privelage; 
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
	
	/*
	 * Set starting date of the event
	 */
	public void setStartDate(){
		int minutes = 0;
		int hours = 0;
		if(startTime.length() == 4){
			minutes = Integer.parseInt(startTime.substring(2,4));
			hours = Integer.parseInt(startTime.substring(0,2));
		}
		else{
			hours = Integer.parseInt(startTime);
		}
		startDate = new Date(Calendar.getInstance().get(Calendar.YEAR) - 1900, Integer.parseInt(startMonth), Integer.parseInt(startDay), hours, minutes);	
	}
	
	/*
	 * Set ending date if the event (optional call)
	 */
	public void setEndDate(){
		int minutes = 0;
		int hours = 0;
		if(endTime.length() == 4){
			minutes = Integer.parseInt(endTime.substring(2,4));
			hours = Integer.parseInt(endTime.substring(0,2));
		}
		else{
			hours = Integer.parseInt(endTime);
		}
		endDate = new Date(Calendar.getInstance().get(Calendar.YEAR) - 1900, Integer.parseInt(endMonth), Integer.parseInt(endDay), hours, minutes);	
	}
	
}