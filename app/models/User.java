package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

import play.db.jpa.*;

@Entity
public class User extends Model {

	public String name; // The user's full name
	public String first_name; // The user's first name
	public String middle_name; // The user's middle name
	public String last_name; // The user's last name
	public String gender; // The user's gender: female or male
	public String locale; // The user's locale (ISO Language Code and ISO Country
												// Code)
	
	public String username; // The user's username
	public float timezone; // The user's timezone offset from UTC
	public Date updated_time; // The last time the user's profile was updated;
														// changes to the
														// languages, link, timezone, verified,
														// interested_in, favorite_athletes, favorite_teams,
														// andvideo_upload_limits are not not reflected in
														// this value
	
	public boolean verified; // The user's account verification status,
														// either true or false(see below)
	public String bio; // The user's biography
	public Date birthday; // The user's birthday
	// @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	// public List<Enrollment> education; // A list of the user's education
	// history
	public String email; // The proxied or contact email address granted by the
												// user

	@OneToOne
	public User significant_other; // The user's significant other
	public Date anniversary; // date of anniversary
	public String website; // The URL of the user's personal website
	
	// not implemented yet!!
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	public List<UserLanguage> languages; // The user's languages
	@ManyToOne
	public Location hometown; // The user's hometown
	// public List<String> interested_in; // The genders the user is interested in
	@ManyToOne
	public Location location; // The user's current city

	public String political; // The user's political view
	public String quotes; // The user's favorite quotes
	public String relationship_status; // The user's relationship
																			// status:Single, In a
																			// relationship, Engaged,Married, It's
																			// complicated, In an open
																			// relationship, Widowed,Separated, Divorced, In
																			// a civil union, In a domestic
																			// partnership
	public String religion; // The user's religion

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	public List<Employment> work; // A list of the user's work history

	public String password;

	@OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
	public List<Relationship> friends; // A list of the user's work history

	@OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
	public List<Relationship> friendedBy; // A list of the user's work history

	public User(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
		// this.education = new ArrayList<Enrollment>();
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public List<Post> news() {
		return Post.find(
				"SELECT p FROM Post p, IN(p.author.friendedBy) u WHERE u.from.id = ?",
				this.id).fetch();
	}

}