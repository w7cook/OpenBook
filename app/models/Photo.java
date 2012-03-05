package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;


@Entity
public class Photo extends Model {
  @Required
  public Blob image;

  @Required
  public Date postedAt;

  public String caption;

  @Required
  @ManyToOne
  public User owner;
}
