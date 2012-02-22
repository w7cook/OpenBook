package models;

import java.util.*;
import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;


/**
 * Skin
 * @author Lauren Yew
 * 
 * This is the Database model for StyleSheets
 * it is similar to the Firefox personas
 * it uses the template OpenBook/app/views/stylesheets/main.css
 * which is called by OpenBook/app/views/tags/main.html
 * 
 * Each User has one Skin. Each Skin can have multiple users.
 * Differences in Skins are in how they use the template main.css
 * Variables will include: 
 * color
 * size
 * shape
 * (more to be added)
 * 
 * From discussion with Professor: No other templates will be added. 
 * The only changes in Skin will be to variables within main.css
 * 
 *
 */
@Entity
public class Skin extends Model {

	public String name;//name of the skin
	public int logoFontSize;
	public String backgroundColor;
	/**
	 * allClients:
	 * Users currently working with this skin
	 */
	@OneToMany(mappedBy = "skin", cascade = CascadeType.ALL)
	public List<User> allClients;

	/**
	 * clients()
	 * @return List(User) 
	 * 
	 * returns list of users using this skin
	 * 
	 */
	public List<User> clients() {
		return User.find("skin = ?", this).fetch();
	}

	public Skin()
	{
		this.allClients = new ArrayList<User>();
		this.name = "DEFAULT";
		this.logoFontSize = 30;
		this.backgroundColor = "000000";//"CC5500";
	}
	
	/**
	 * Skin specified constructor
	 * @param title (name of the skin upon creation)
	 */
	public Skin(String title) {
		this.allClients = new ArrayList<User>();
		this.name = title;
		this.logoFontSize = 18;
	}

	/**
	 * addClient
	 * @param User client
	 * @return Skin 
	 * 
	 * adds User client to the list of clients for this skin
	 * returns the skin that the client requested
	 */
	public Skin addClient(User client) {
		this.allClients.add(client);
		this.save();
		return this;
	}

	/**
	 * numClients
	 * @return (long) number of clients using this skin
	 * 
	 */
	public long numClients() {
		return Skin.find("Count(*)").first();
	}
	
	/**
	 * clientIsCurrentUser
	 * @return boolean (whether or not the currentUser is using this skin)
	 */
	public boolean clientIsCurrentUser() {
		User client = User.find("skin = ? AND email = ?", this, Security.connected()).first();
		if(client == null)
			return false;
		else
			return true;
			
	}
		
}
