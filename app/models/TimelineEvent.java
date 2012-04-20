package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;

class TimelineEvent extends Commentable {

@Required
@ManyToOne
public Timeline parentTimeline; //Reference to the Owner's timeline


@OneToOne(mappedBy="timelineevent", cascade=CascadeType.ALL)
public Post post; //The post that represents this timeline event
private Vector<User> participants;
private Object relatedObject; //The Object involved in the event (could be anything)
private Date timeOfEvent; //The time the event happened (note, can be different from relatedEvent's time)
private Timeline.Action action; // An enum that will represent what was done with the related object


public TimelineEvent(Timeline t, Object o, Timeline.Action action, Vector<User> participants){
this.parentTimeline = t;
this.relatedObject = o;
this.participants = participants;
this.timeOfEvent = new Date();
this.action = action;

parseAction(action, o);	//generate post from action & object info
}

/**
 * This method will parse the given action and the Object associated with it to create a meaningful post for this TimelineEvent. By using the JAVA reflection API along with the enumerated actions,
 * along with the constants language shortcuts defined, A meaningful message is generated, and put in a post, which is put in the post list for the timeline.
 * 
 *
 *  **/ 
private void parseAction(Timeline.Action action, Object o){
	String subject = "";
	if (o instanceof Event){
		subject = "event";
	} else if (o instanceof Group) {
		subject = "group";
	} else if (o instanceof Photo) {
		subject = "photo";
	} else if (o instanceof Status) {
		subject = "status";
	} else if (o instanceof Subscription) {
		subject = "subscription";
	} else if (o instanceof Album) {
		subject = "album";
	} else if (o instanceof Comment) {
		subject = "comment";
	} else if (o instanceof Link) {
		subject = "link";
	}
	
	//Note/TODO : every model should probably have a welformed toString or atleast getReadableName so that good names can be generated... 
	
	
	String msg = "";
	if (action == Timeline.Action.CREATE){
		msg = this.parentTimeline.author.name + " made a new " + subject + "\n";
	} else if (action == Timeline.Action.MODIFY){
		msg = this.parentTimeline.author.name + " made changes to an " + subject + "\n";
	} else if (action == Timeline.Action.DELETE){
		msg = this.parentTimeline.author.name + " deleted  " + subject + "\n"; 
	}
		
	this.post = new Post(null, this.parentTimeline.author, msg); //need to find out what that first thing is supposed to be.
}


}

