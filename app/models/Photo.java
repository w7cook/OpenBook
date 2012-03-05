package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Photo extends Model {
  @Required
  public Blob image;

  public String caption;

  @Required
  @ManyToOne
  public User owner;

  public Photo(User owner, Blob image) {
    this.owner = owner;
    this.image = image;
  }
}
