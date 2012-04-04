package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Photo extends Status {
  @Required
  public Blob image;

  @Required
  @ManyToOne
  public User owner;

  public Photo(User owner, Blob image) {
    super(owner, null);
    this.owner = owner;
    this.image = image;
  }
  
  public Photo(User owner, Blob image, String caption){
    super(owner, caption);
    this.image = image;
  }
}
