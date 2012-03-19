package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;
import play.db.jpa.*;
import play.data.validation.*;
import play.libs.Crypto;

@Table(name="Openbook_Group")
@Entity
public class Group extends Model{
	
	@Required
	@ManyToOne
	public User owner;
	
	@Required
	@Lob
	public String groupName;
	
	@OneToMany
	public List<User> members;
	
	public Group(User o, String n){
		this.owner= o;
		this.groupName= n;
		this.members= new ArrayList<User>();
		this.members.add(o);
	}
	
	public void addMember(User u){
		if(!members.contains(u))
			members.add(u);
	}
	
	public void removeMember(User u){
		if(members.contains(u))
			members.remove(u);
	}
	
	public int getMemberCount(){
		return members.size();
	}
}
