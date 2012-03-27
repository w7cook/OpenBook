package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;
import models.Post;
import play.db.jpa.*;
import play.data.validation.*;
import play.libs.Crypto;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

@ElasticSearchable
@Table(name="Openbook_Group")
@Entity
public class Group extends Model{
	
	@Required
	@ManyToOne
	public User owner;
	
	@Required
	@Lob
	public String groupName;
	
	@Required
	@Lob
	public String description;
	
	@ElasticSearchIgnore
	@OneToMany
	public List<User> members;
	
	public Group(User o, String n, String d){
		this.owner= o;
		this.groupName= n;
		this.description= d;
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
	
	public List<Post> getPosts(){
      return Post.find("SELECT p FROM Post p WHERE p.postType = ? and p.title = ? order by p.updatedAt desc",Post.type.GROUP,this.id.toString()).fetch();
  }
}
