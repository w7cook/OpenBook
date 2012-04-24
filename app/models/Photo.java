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
  
  public Long entityId;
  public enum type{USER, PAGE,GROUP,EVENT};
  public type photoType;

  public Photo(User owner, Blob image) {
    super(owner, owner, "");
    this.owner = owner;
    this.image = image;
    this.photoType = type.USER;
    this.entityId = owner.id;//default owner id
  }
  /**
   * photo contructor for P/G/E
   *
   * @param   				owner = uploader
   *									image = the image to be uploaded
   *									id = the id of the entity(page/group/event) the image belongs to 
   *									p = the post type for post super class
   *									t = the photo type for the photo
   */
  public Photo(User owner, Blob image, Long id, Photo.type t, Postable p) {
  	super(p, owner, "");
    this.owner = owner;
    this.image = image;
    this.entityId = id;
    this.photoType = type.PAGE;
  }
  
  public Photo(User owner, Blob image, String caption){
    super(owner, owner, caption);
    this.image = image;
  }
}
