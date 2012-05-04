package models;

import java.io.*;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.*;

import utils.Bootstrap;

import controllers.Photos;
import controllers.Skins;

@Entity
public class Profile extends Model {
  @OneToOne(cascade=CascadeType.ALL)
  public User owner;

  public String gender; // The user's gender:female or male
  public String locale; // The user's locale (ISO Language Code and ISO Country
  public String gravatarEmail;

  @OneToOne
  public User significantOther; // The user's significant other
  @Match("\\^\\(0\\[1-9\\]\\|1\\[012\\]\\)\\[-/.\\]\\(0\\[1-9\\]\\|\\[12\\]\\[0-9\\]\\|3\\[01\\]\\)\\[-/.\\]\\(19\\|20\\)\\d\\d\\$")
  public Date anniversary; // date of anniversary

  public String bio; // The user's biography
  public String interestedIn; //genders the user is intersted in: Male, Female, Both, Neither

  @OneToOne
  public Photo profilePhoto; // The user's profile picture.
  @OneToOne
  public Photo gravatarPhoto;

  @Match("\\^\\(0\\[1-9\\]\\|1\\[012\\]\\)\\[-/.\\]\\(0\\[1-9\\]\\|\\[12\\]\\[0-9\\]\\|3\\[01\\]\\)\\[-/.\\]\\(19\\|20\\)\\d\\d\\$")
  public Date birthday; // The user's birthday, uses JQuery UI

  @ManyToOne
  public Location location; // The user's current city

  @ManyToOne
  public Location hometown; // The user's hometown

  // not implemented yet!!
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  public List<UserLanguage> languages; // The user's languages

  public String political; // The user's political view
  public String quotes; // The user's favorite quotes

  public enum Relationship {
    SINGLE ("Single"),
    ENGAGED ("Engaged"),
    MARRIED ("Married"),
    ITSCOMPLICATED ("It's complicated"),
    OPEN ("Open relationship"),
    WIDOWED ("Widowed"),
    SEPERATED ("Seperated"),
    DIVORCED ("Divorced"),
    CIVILUNION ("Civil union"),
    DOMESTIC ("Domestic partnership");

    private final String text;
    Relationship(String text) {
      this.text = text;
    }

    public String toString() {
      return text;
    }

    public static Relationship fromString(String text) {
      for (Relationship r : Relationship.values())
        if (r.text.equalsIgnoreCase(text))
          return r;
      return null;
    }
  }
  public Relationship relationshipStatus; // The user's relationship

  private static final Relationship[] singleArray = {Relationship.SINGLE, Relationship.ITSCOMPLICATED, Relationship.WIDOWED, Relationship.SEPERATED, Relationship.DIVORCED};
  public static final HashSet<Relationship> single =  new HashSet(Arrays.asList(singleArray));

  // status:Single,In a relationship, Engaged,Married,It's
  // complicated,In an open relationship,Widowed,Separated,Divorced,In
  // a civil union,In a domestic partnership

  public String religion; // The user's religious views

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  public List<Enrollment> education; // A list of the user's education history

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  public List<Employment> work; // A list of the user's work history

  @ManyToOne
  public Skin skin;//Skin (StyleSheet) used by this Profile

  @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
  public List<RSSFeed> feeds;

  //CONTACT INFORMATION
  @Phone public String phone; // the user's phone number
  public String address; // the user's address
  public String website; // the user's website
  public String email; //the user's email

  public boolean hasAnniversary() {
    return !single.contains(relationshipStatus);
  }

  public Profile(User owner) {
    this.anniversary = null;
    this.bio = "";
    this.birthday = null;
    this.interestedIn = "";
    this.relationshipStatus = Relationship.SINGLE;
    this.gender = "";
    this.hometown = null;
    this.location = null;
    this.owner = owner;
    this.political = "";
    this.quotes = "";
    this.significantOther = null;
    this.religion = "";

    this.languages = new ArrayList<UserLanguage>();
    this.education = new ArrayList<Enrollment>();
    this.work = new ArrayList<Employment>();
    this.feeds = new ArrayList<RSSFeed>();
    this.skin = Skins.getSkin("default","default_skin");//the default skin look is used
    this.phone = "";
    this.address = "";
    this.website = "";
    this.email = "";
    User defaultUser = User.find("username= ?", "default").first();
    this.profilePhoto = Photo.find("owner=? AND caption=?",defaultUser, "Default Profile Photo").first();
    this.gravatarPhoto = null;
    this.gravatarEmail = owner.email;
  }

  public Profile(User owner, String bio, String gender, String quotes, String phone, String website) {
    this.owner = owner;
    this.bio = bio;
    this.gender = gender;
    this.phone = phone;
    this.quotes = quotes;
    this.website = website;
    User defaultUser = User.find("username= ?", "default").first();
    this.profilePhoto = Photo.find("owner=? AND caption=?",defaultUser, "Default Profile Photo").first();
  }
}
