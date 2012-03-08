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
  public User significantOther; // The user's significant other
  public Date anniversary; // date of anniversary

  public String bio; // The user's biography
  public String interestedIn; //genders the user is intersted in: Male, Female, Both, Neither


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
  public String relationshipStatus; // The user's relationship
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
  	return !(relationshipStatus.equals("Single") || relationshipStatus.equals("It's complicated")
  		|| relationshipStatus.equals("Widowed") || relationshipStatus.equals("Separated") || relationshipStatus.equals("Divorced"));
  }

  public Profile(User owner) {

    this.anniversary = null;
    this.bio = "";
    this.birthday = null;
    this.interestedIn = "";
    this.relationshipStatus = "Single";
    this.gender = "";
    this.hometown = null;
    this.location = null;
    this.owner = owner;
    this.political = "";
    this.quotes = "";
    this.significantOther = null;
    this.religion = "";

    this.education = new ArrayList<Enrollment>();
    this.work = new ArrayList<Employment>();

    this.phone = "";
    this.address = "";
    this.website = "";
    this.email = "";

  }
}