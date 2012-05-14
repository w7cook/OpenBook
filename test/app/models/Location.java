package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.*;

@Entity
public class Location extends Model {
	String location;
	
	public Location(String name){
		location = name;
	}
	
	public String toString(){
		return location;
	}
}
