package models;

import java.util.*;
import javax.persistence.*;

import org.elasticsearch.index.query.QueryBuilders;
import controllers.Application;
import controllers.Skins;
import controllers.Users;
import play.db.jpa.*;
import play.modules.elasticsearch.*;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.modules.elasticsearch.search.SearchResults;
import play.libs.Crypto;

@Entity
public class Page extends Model {
	
	@OneToOne
	public User admin;
	
	@Lob
	public String info;
	public String title;
	
	public Page(User user, String newTitle, String newInfo){
		this.admin = user;
		this.title = newTitle;
		this.info = newInfo;
	}

}
