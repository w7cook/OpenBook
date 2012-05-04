package models;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Link extends Post {
  enum source {VIDEO, IMAGE, OTHER};
  private static final Pattern youtube_pattern = Pattern.compile("");
  private static final Pattern vimeo_pattern = Pattern.compile("(http://)?(www.)?vimeo.com/[0-9]+");
  private static final Pattern image_pattern = Pattern.compile(".*[\\.png | \\.jpeg | \\.gif]$");
  private static final String vimeo_embed = "http://player.vimeo.com/video/";
  
  public String external_url;
  public String link_type;
  
  
  public Link (User user, String content, String url){
    super(user, user, content);
    this.external_url = parseLink(url);
  }
  
  private String parseLink(String content){
    Matcher youtube = youtube_pattern.matcher(content);
    Matcher vimeo = vimeo_pattern.matcher(content);
    Matcher image = image_pattern.matcher(content);
    
    if(vimeo.matches()){
      this.link_type = source.VIDEO.toString();
      content.replace("(http://)?(www.)?vimeo.com/", "");
      content = vimeo_embed + content;;
    }
    else if(image.matches()){
      this.link_type = source.IMAGE.toString();
    }
    else{
      this.link_type = source.OTHER.toString();
    }
    return content;
  }


}
