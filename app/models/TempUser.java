package models;

import java.util.*;
import javax.persistence.*;

import play.libs.Crypto;

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