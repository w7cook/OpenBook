/**Album
 * purpose: Defines the
 * 
 * created by: Chris. Cale
 * 
 * email: collegeassignment@gmail.com
 * 
 */

package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Album extends Model{

  
//  @Id
//  public Long albumId;
  
  @Required
  public String title;
  @Required
  public Date dateCreated;
  @Required
  public Date lastDateModified;
  
  @ManyToOne
  @Required
  public User owner;
  
  @OneToMany//(mappedBy = "Album",cascade = CascadeType.ALL)
  public List<Photo> photos;
  
  
  /**Default constructor
   * 
   * @param albumName the name of the photo album is set to "untitled"
   * @param albumId the automatically generated UIG for this album
   * @param user the current user is  set as the owner of this album
   * @param dateCreated the date that the album was created
   * @param lastDateModified the last date album was modified by the user
   * 
   * @return an album that contains photos
   */
  public Album(User owner){//default constructor
    this.title = "untitled";
    //this.albumId = getId();
    this.owner = owner;
    this.dateCreated = new Date();
    this.lastDateModified = new Date();
    this.photos = new ArrayList<Photo>();
  }
  
  /**Non-default constructor
   * 
   * @param albumName the name of the photo album to user String input
   * @param albumId the automatically generated UIG for this album
   * @param user the current user is  set as the owner of this album
   * @param dateCreated the date that the album was created
   * @param lastDateModified the last date album was modified by the user
   * 
   * @return an album that contains photos
   */
  public Album(String albumName, User owner){
    this.title = albumName;
    //this.albumId = getId();
    this.owner = owner;
    this.dateCreated = new Date();
    this.lastDateModified = new Date();
    this.photos = new ArrayList<Photo>();
  }


  
}//end of class
