package controllers;

import java.io.*;
import play.*;
import play.data.validation.Error;
import play.libs.*;
import play.libs.WS.*;
import play.mvc.*;
import play.db.jpa.*;
import models.*;

import java.util.*;
import java.util.regex.*;


@With(Secure.class)
public class Videos extends OBController {
  
  
  public static void listLinkedVideos(long user_id){
    User user = User.findById(user_id);
    List<LinkedVideo> videos = LinkedVideo.find("owner = ? order by createdAt asc", user).fetch();
    render(videos);
  }
  
  public static void uploadLinkedVideo(){
  }
  
  public static void showLinkedVideo(Long linkedVideoID){
    LinkedVideo vid = LinkedVideo.findById(linkedVideoID);
    render(vid);
  }
  
  public static void createLinkedVideo(String video_title, String video_link, String video_caption){
    validation.required(video_title).message("Video title is required");
    validation.required(video_link).message("Link must not be blank");
    validation.url(video_link).message("Link must be a valid URL (ex. http://www.example.com)");
    
    char link_type = decipherLinkType(video_link);
    validation.isTrue(link_type != '\u0000').message("Link must be a valid vimeo, youtube or dailymotion video link.");
    
    Logger.info("A log message: " + link_type);     
    validation.isTrue(isValidVideo(video_link)).message("Link must be a valid video");
    
    User usr = user();
    
    if(validation.hasErrors()){
      renderTemplate("Videos/listLinkedVideos.html");
    }
    else{
            
      LinkedVideo new_vid = new LinkedVideo(usr, video_title, video_link, video_caption, (int)link_type, getVideoId(video_link, link_type));
      
      new_vid.likes = new ArrayList<Likes>();
      new_vid.comments = new ArrayList<Comment>();
      new_vid.save();
      showLinkedVideo(new_vid.id);
    }
    
  }

  
  public static void uploadVideo(){
    
  }
  
  public static void createUploadedVideo(){
    
  }
  
  public static char decipherLinkType(String link){
    
    Pattern YOUTUBE = Pattern.compile("(?:https?://)?(?:www\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)[?=&+%\\w]*");
    Pattern DAILYMOTION = Pattern.compile("(?:https?://)?(?:www\\.)?dailymotion\\.com/video/([\\w\\d]{6})(?:_.*)?");
    Pattern VIMEO = Pattern.compile("(?:https?://)?(?:www\\.)?vimeo\\.com/(\\d)+");
    
    Matcher y = YOUTUBE.matcher(link);
    Matcher d = DAILYMOTION.matcher(link);
    Matcher v = VIMEO.matcher(link);
    
    if (y.matches())
      return 'y';
    else if(d.matches())
      return 'd';
    else if(v.matches())
      return 'v';
    else
      return '\u0000';
    
  }
  
  public static String getVideoId(String link, char link_type){
    
    Pattern YOUTUBE = Pattern.compile("https?://(?:www\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)[?=&+%\\w]*");
    Pattern DAILYMOTION = Pattern.compile("https?://(?:www\\.)?dailymotion\\.com/video/([\\w\\d]{6})(?:_.*)?");
    Pattern VIMEO = Pattern.compile("https?://(?:www\\.)?vimeo\\.com/(\\d)+");
    
    Matcher m;
    String videoId = "";
    if(link_type == 'y'){
      m = YOUTUBE.matcher(link);
      if(m.find()){
        videoId = m.group(1);
      } 
    }
    
    return videoId;
  }
  
  public static String getThumb(long videoId){
    LinkedVideo vid = LinkedVideo.findById(videoId);
    
    if(vid.link_type == 'y'){
      return "http://img.youtube.com/vi/" + vid.video_id + "/1.jpg";
    }
    
    return "blah";
  }
  
  private static boolean isValidVideo(String link){
    WS web_serv = new WS();
    WS.WSRequest ws_request = web_serv.url(link);
    WS.HttpResponse response = ws_request.get();
    
    if(response.getStatus() == 200 || response.getStatus() == 301)
      return true;
    else
      return false;
  }
  
  public static void addComment(long vid_id, String comment){
    LinkedVideo vid = LinkedVideo.findById(vid_id);
    vid.addComment(user(), comment);
    
    showLinkedVideo(vid_id);
  }
  
  public static void removeComment(long comment_id){
    Comment com = Comment.findById(comment_id);
    LinkedVideo vid = (LinkedVideo) com.parentObj;
    if (!com.author.equals(user()))
      forbidden();
    com.delete();
    
    showLinkedVideo(vid.id);
    
  }
  
  public static void removeLinkedVideo(long video_id){
    LinkedVideo vid = LinkedVideo.findById(video_id);
    User owner = vid.owner;
    if (!owner.equals(user()))
      forbidden();  
    vid.delete();
    
    listLinkedVideos(owner.id);
  }
  
    
  
}