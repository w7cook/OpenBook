package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Subscription extends Model {
	
	@ManyToOne
	public User subscribed;
	
	@ManyToOne
	public User subscriber;
	
	public Subscription(User subscribed, User subscriber) {
		this.subscribed = subscribed;
		this.subscriber = subscriber;
	}

}
