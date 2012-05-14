package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.regex.*;

@Entity
public class LinkedVideo extends Post {
  
  @Required
  @URL public String link;
  
  public int link_type;
  public String video_id;
  
  public String thumbnail_url;
  
  @Required
  public String title;

  public String caption;
  
  @Required
	@ManyToOne
	@JoinTable(name="LinkedVideoToUser")
	public User owner;

  
  public LinkedVideo(User owner, String title, String link, String caption, int link_type, String video_id){
    super(owner, owner, "");
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
  // 1 = small 2 = big
  protected String getEmbedHtml(int size){
    
    if(size == 1){
      if(link_type == 'y')
        return "<iframe class=\"youtube-player\" type=\"text/html\" width=\"420\" height=\"255\" src=\"http://www.youtube.com/embed/" + video_id +  "\" frameborder=\"0\"> </iframe>";
      else if (link_type == 'd')
        return "<iframe src=\"http://www.dailymotion.com/embed/video/" + video_id + "/\" width=\"420\" height=\"255\" frameborder=\"0\"></iframe>";
      else if(link_type == 'v')
        return "<iframe src=\"http://player.vimeo.com/video/" + video_id + "\" width=\"420\" height=\"255\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>";
      else
        return "";
    }
    else if(size == 2){
      if(link_type == 'y')
        return "<iframe class=\"youtube-player\" type=\"text/html\" width=\"560\" height=\"340\" src=\"http://www.youtube.com/embed/" + video_id +  "\" frameborder=\"0\"> </iframe>";
      else if (link_type == 'd')
        return "<iframe src=\"http://www.dailymotion.com/embed/video/" + video_id + "/\" width=\"560\" height=\"340\" frameborder=\"0\"></iframe>";
      else if(link_type == 'v')
        return "<iframe src=\"http://player.vimeo.com/video/" + video_id + "\" width=\"560\" height=\"340\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>";
      }
      
      return "";
  }
  
  public List<Comment> comments() {
    return Comment.find("parentObj = ? order by createdAt asc", this).fetch();
  }
  
 
  
  
}
