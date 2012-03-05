package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

import play.db.jpa.*;

@Entity
public class Page extends Model {
		
	public String title;
	
	@Lob
	public String info;
	
	public ArrayList<Comment> comments;
	public String admin;
	
	public Page(){
		this.comments = new ArrayList<Comment>();
	}
	
	public Page(User user, String name, String info){
		this.comments = new ArrayList<Comment>();
		this.title = name;
		this.admin = user.email;
		this.info = info;
	}
	
	public void update(String info){
		this.info = info;
	}
	
}
