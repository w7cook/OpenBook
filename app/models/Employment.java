package models;

import java.util.Date;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Employment extends Model {
  @ManyToOne
  public Profile employee;
  public String employer;
  Location location;
  public String position;
  public Date start_date;
  public Date end_date;

  public Employment(String employer){
	//this.employee = employee;
	this.employer = employer;
}

  public String toString()
  {
  	return employer;
  }
}