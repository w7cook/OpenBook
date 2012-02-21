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
}
