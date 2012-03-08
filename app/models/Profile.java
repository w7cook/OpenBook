package models;

import java.util.*;

import javax.persistence.*;

import controllers.Application;

import play.db.jpa.*;

@Entity
public class Profile extends Model {
  @OneToOne
  public User owner;
  public String gender; // The user's gender: female or male
  public String locale; // The user's locale (ISO Language Code and ISO Country

  @OneToOne
  public User significant_other; // The user's significant other
  public Date anniversary; // date of anniversary

  public String bio; // The user's biography
  public String interested_in; //genders the user is intersted in: Male, Female, Both, Neither


  public Date birthday; // The user's birthday, uses javascript: http://www.dynamicdrive.com/dynamicindex7/jasoncalendar.htm

  @ManyToOne
  public Location location; // The user's current city
  @ManyToOne
  public Location hometown; // The user's hometown

  // not implemented yet!!
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  public List<UserLanguage> languages; // The user's languages

  public String political; // The user's political view
  public String quotes; // The user's favorite quotes
  public String relationship_status; // The user's relationship
  // status:Single,In a relationship, Engaged,Married,It's
  // complicated, In an open relationship, Widowed,Separated, Divorced, In
  // a civil union, In a domestic partnership

  public String religion; // The user's religious views

   @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  public List<Enrollment> education; // A list of the user's education history

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  public List<Employment> work; // A list of the user's work history


  //CONTACT INFORMATION
  public String phone; // the user's phone number
  public String address; // the user's address
  public String website; // the user's website
  public String email; //the user's email

  public boolean hasAnniversary()
  {
  	return !(relationship_status.equals("Single") || relationship_status.equals("It's complicated")
  		|| relationship_status.equals("Widowed") || relationship_status.equals("Separated") || relationship_status.equals("Divorced"));
  }

  public Profile(User owner) {

    this.anniversary = null;
    this.bio = "";
    this.birthday = null;
    this.interested_in = "";
    this.relationship_status = "Single";
    this.gender = "";
    this.hometown = null;
    this.location = null;
    this.owner = owner;
    this.political = "";
    this.quotes = "";
    this.significant_other = null;
    this.religion = "";

    this.education = new ArrayList<Enrollment>();
    this.work = new ArrayList<Employment>();

    this.phone = "";
    this.address = "";
    this.website = "";
    this.email = "";

  }
}