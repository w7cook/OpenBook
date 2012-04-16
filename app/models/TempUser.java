package models;

import java.util.*;
import javax.persistence.*;

import org.elasticsearch.index.query.QueryBuilders;
import controllers.Application;
import controllers.Messages;
import controllers.Skins;
import controllers.Users;
import play.db.jpa.*;
import play.modules.elasticsearch.*;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.modules.elasticsearch.search.SearchResults;
import play.libs.Crypto;

@ElasticSearchable
@Entity
public class TempUser extends Model {
  public String first_name; // The user's first name
  public String last_name; // The user's last name
  public String username; // The user's username
  public boolean verified; // The user's account verification status,
  public String email; // The proxied or contact email address granted by the
  public String password;
  public String UUID;

  public TempUser(String email, String password, String username, String first_name, String last_name, String UUID) {
    this.email = email;
    this.password = Crypto.passwordHash(password);
    this.username = username;
    this.first_name = first_name;
    this.last_name = last_name;
    this.UUID = UUID;
    verified = false;
  }
}