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
public class UserPage extends Model {
	
	@ManyToOne
	public Page page;
	
	@ManyToOne
	public User fan;
	
	public UserPage(User user, Page page){
		this.fan = user;
		this.page = page;
	}
}
