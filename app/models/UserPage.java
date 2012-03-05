package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

import play.db.jpa.*;

@Entity
public class UserPage extends Model {
	
	@ManyToOne
	public Page page;

	@ManyToOne
	public User fan;
	
	public UserPage(Page pagina, User user){
		this.page = pagina;
		this.fan = user;
	}
	
	public UserPage(){
	}
	
	public Page getPage(){
		return this.page;
	}
}
