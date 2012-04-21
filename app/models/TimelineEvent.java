package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.Date;
import java.util.Vector;

class TimelineEvent {

@Required
@ManyToOne
public TimelineModel parentTimeline; //Reference to the Owner's timeline


@OneToOne(mappedBy="timelineevent", cascade=CascadeType.ALL)
public Post post; //The post that represents this timeline event
private Vector<User> participants;
private String foreword;
private Object relatedObject; //The Object involved in the event (could be anything)
private Date timeOfEvent; //The time the event happened (note, can be different from relatedEvent's time)
private TimelineModel.Action action; // An enum that will represent what was done with the related object

/**
 * Create a new TimelineEvent
 * @param t The TimelineModel object that this TimelineEvent will be linked to
 * @param o The Object that is associated with the TimelineEvent (could be a group, event, etc)
 * @param action The action that occurred (an enum type, ex. CREATE | MODIFY | DESTROY)
 * @param participants The participants in the timeline event
 * @param foreword A string that will be used to preface the object when the underlying post (inside TimelineEvent) is made
 * */
public TimelineEvent(TimelineModel t, Object o, TimelineModel.Action action, Vector<User> participants, String foreword){
this.parentTimeline = t;
this.relatedObject = o;
this.participants = participants;
this.timeOfEvent = new Date();
this.action = action;
this.foreword = foreword;

parseAction();  //generate post from action & object info
}

/**
 * Parse the contents of this TimelineEvent to generate the content of it's post.
 * */
private void parseAction(){
        String subject = "";
        if (this.relatedObject instanceof Event){
                subject = "event";
        } else if (this.relatedObject instanceof Group) {
                subject = "group";
        } else if (this.relatedObject instanceof Photo) {
                subject = "photo";
        } else if (this.relatedObject instanceof Status) {
                subject = "status";
        } else if (this.relatedObject instanceof Subscription) {
                subject = "subscription";
        } else if (this.relatedObject instanceof Album) {
                subject = "album";
        } else if (this.relatedObject instanceof Comment) {
                subject = "comment";
        } else if (this.relatedObject instanceof Link) {
                subject = "link";
        }

        //<TODO> every model should probably have a welformed toString or atleast getReadableName so that good names can be generated...

        String msg = "";
        if (action == TimelineModel.Action.CREATE){
                msg = foreword + subject + "\n";
        } else if (action == TimelineModel.Action.MODIFY){
                msg = foreword + subject + "\n";
        } else if (action == TimelineModel.Action.DELETE){
                msg = foreword + subject + "\n";
        }

        this.post = new Post(null, this.parentTimeline.owner, msg); //need to find out what that first thing is supposed to be.
}


}

