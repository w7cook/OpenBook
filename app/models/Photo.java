package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Photo extends Post {
  @Required
  public Blob image;
  
  @Required
  @ManyToOne
  public User owner;
  
  @ManyToOne
  public Page page; 

  public Photo(User owner, Blob image) {
    super(owner, "", "");
    this.owner = owner;
    this.image = image;
  }
  
  public Photo(User owner, Blob image, Page p) {
    super(owner, "", "", Post.type.PAGE);
    this.owner = owner;
    this.image = image;
    this.page = p;
  }
  
  public Photo(User owner, Blob image, String caption){
    super(owner, "", caption);
    this.image = image;
  }
}
