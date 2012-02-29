package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

import play.db.jpa.*;

@Entity
public class UserPage extends Model {
	public String title;
	public Long Admin;
	public String info;
	public ArrayList<Comment> comments;
	

	//public List<User> fans;
	
	public UserPage(){
		this.comments = new ArrayList<Comment>();
	}
	
	public UserPage(User admin, String name, String info){
		this.comments = new ArrayList<Comment>();
		this.title = name;
		this.Admin = admin.id;
		this.info = info;
	}
	
	public void setData(User admin, String title, String info){
		this.title = title;
		this.Admin = admin.id;
		this.info = info;
	}

	public void setInfo(String nInfo){
		this.info = nInfo;
	}
}
