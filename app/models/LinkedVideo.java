package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.regex.*;

@Entity
public class LinkedVideo extends Commentable {
  
  @Required
  @URL public String link;
  
  public int link_type;
  public String video_id;
  
  @Required
  public String title;

  public String caption;

  @Required
  @ManyToOne
  public User owner;
  
  public LinkedVideo(User owner, String title, String link, String caption, int link_type, String video_id){
    this.title = title;
    this.owner = owner;
    this.link = link;
    this.caption = caption;
    this.link_type = link_type;
    this.video_id = video_id;
    
  }
  
  public void addCaption(String caption){
    this.caption = caption;
  }
  
  protected String getEmbedHtml(){
    if(link_type == 'y')
      return "<iframe class=\"youtube-player\" type=\"text/html\" width=\"560\" height=\"340\" src=\"http://www.youtube.com/embed/" + video_id +  "\" frameborder=\"0\"> </iframe>";
    else if (link_type == 'd')
      return "";
    else if(link_type == 'v')
      return "";
    else
      return "NOOOO";
  }
  
  public List<Comment> comments() {
    return Comment.find("parentObj = ? order by createdAt asc", this).fetch();
  }
  
 
  
  
}
