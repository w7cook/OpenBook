package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.*;

@Entity
public class Enrollment extends Model {

  public User student;

  public String name;
  public String type;
  public Date year;
  public String degree;
  public String concentration; // array!
  public String classes; // array!

  public String toString()
  {
  	return name;
  }
}
