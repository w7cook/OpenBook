package models;

import java.util.Date;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Enrollment extends Model {
  @ManyToOne
  public Profile student;

  public String name;
  public String type;
  public Date year;
  public String degree;
  public String concentration; // array!
  public String classes; // array!


  public Enrollment(String name){
	this.name = name;
}

  public String toString()
  {
  	return name;
  }
}
