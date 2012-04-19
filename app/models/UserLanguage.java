package models;

import java.util.Date;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class UserLanguage extends Model {
	@ManyToOne
  public User user;

	@ManyToOne
  public Language language;

	public UserLanguage(User user, Language lang){
		this.user  = user;
		this.language = lang;
	}
}