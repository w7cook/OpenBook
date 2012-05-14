package models;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Link extends Post {
  enum source {VIDEO};
  private static final Pattern youtube_pattern = Pattern.compile("");
  private static final Pattern vimeo_pattern = Pattern.compile("");
  
  public String external_url;
  public source link_type;
  
  
  public Link (User user, String content, String url){
    super(user, user, content);
    this.external_url = parseLink(url);
  }
  
  private String parseLink(String content){
    Matcher youtube = youtube_pattern.matcher(content);
    Matcher vimeo = vimeo_pattern.matcher(content);
    
    this.link_type = source.VIDEO;
    return content;
  }


}
