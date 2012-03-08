package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.*;

@Entity
public class Employment extends Model {

  public User employee;
  public String employer;
  Location location;
  public String position;
  public Date start_date;
  public Date end_date;

  public String toString()
  {
  	return employer;
  }
}