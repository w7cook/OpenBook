package models;

import java.util.Date;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class UserLanguage extends Model {
	@ManyToOne
  public Profile user;

	@ManyToOne
  public Language language;

	public UserLanguage(Profile user, Language lang){
		this.user  = user;
		this.language = lang;
	}
}