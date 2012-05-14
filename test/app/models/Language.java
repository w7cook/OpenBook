package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.*;

@Entity
public class Language extends Model {
  public String name;

	public Language (String name){
		this.name = name;
	}
}
