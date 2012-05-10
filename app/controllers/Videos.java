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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.IOException;

import java.nio.charset.Charset;
import java.io.Reader;
import java.net.URL;
import java.net.MalformedURLException;

import com.google.gson.*;


@With(Secure.class)
public class Videos extends OBController {
  
  
  public static void listLinkedVideos(long ownerId){
    User user = User.findById(ownerId);
    List<LinkedVideo> videos = LinkedVideo.find("owner = ? order by createdAt asc", user).fetch();
    render(videos);
  }
  
  public static void listMyLinkedVideos(){
    User user = user();
    
    if(user == null)
      forbidden();
    
    List<LinkedVideo> videos = LinkedVideo.find("owner = ? order by createdAt asc", user).fetch();
    renderTemplate("Videos/listLinkedVideos.html", videos);
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
      
      //        
      // new_vid.thoseWhoLike = new HashSet<User>();
      //       new_vid.comments = new ArrayList<Comment>();
      
      new_vid.thumbnail_url = getThumb(new_vid.video_id, link_type);
      
      new_vid.save();
  
      showLinkedVideo(new_vid.id);
    }
    
  }

  
  public static void uploadVideo(){
    
  }
  
  public static void createUploadedVideo(){
    
  }
  
  public static char decipherLinkType(String link){
    
    Pattern YOUTUBE = Pattern.compile("(?:https?://)?(?:www\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)[?=&+%\\w\\-]*");
    Pattern DAILYMOTION = Pattern.compile("(?:https?://)?(?:www\\.)?dailymotion\\.com/video/([\\w\\d]{6})(?:_.*)?");
    Pattern VIMEO = Pattern.compile("(?:https?://)?(?:www\\.)?vimeo\\.com/([\\d]+)");
    
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
    
    Pattern YOUTUBE = Pattern.compile("https?://(?:www\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)[?=&+%\\w\\-]*");
    Pattern DAILYMOTION = Pattern.compile("https?://(?:www\\.)?dailymotion\\.com/video/([\\w\\d]{6})(?:_.*)?");
    Pattern VIMEO = Pattern.compile("https?://(?:www\\.)?vimeo\\.com/([\\d]+)");
    
    Matcher m;
    String videoId = "";
    if(link_type == 'y'){
      m = YOUTUBE.matcher(link);
      if(m.find()){
        videoId = m.group(1);
      } 
    }
    else if(link_type == 'd'){
      m = DAILYMOTION.matcher(link);
      if(m.find()){
        videoId = m.group(1);
      }
    }
    else if(link_type == 'v'){
      m = VIMEO.matcher(link);
      if(m.find()){
        videoId = m.group(1);
        Logger.info("MGROUP: " + m.group(1));
      }
    }
    
    return videoId;
  }
  
  public static String getThumb(String video_id, char link_type){
    
    if(link_type == 'y'){
      return "http://img.youtube.com/vi/" + video_id + "/1.jpg";
    }
    else if(link_type =='d'){
      return "http://www.dailymotion.com/thumbnail/160x120/video/" + video_id + "/";
    }
    else if(link_type =='v'){
      String info_url = "http://www.vimeo.com/api/v2/video/" + video_id + ".json";
      String json_text = jsonStringFromUrl(info_url);
      
      Logger.info("JSON TEXT: " + json_text);
      
      JsonParser parser = new JsonParser();
      
      JsonArray json_array = (JsonArray)parser.parse(json_text);
      JsonElement json_element = json_array.get(0);
      
      JsonObject json_object = (JsonObject)parser.parse(json_element.toString()); 
      
      
      return json_object.get("thumbnail_small").getAsString();
      
    }
    
    return "blah";
  }
  
  public static String jsonStringFromUrl (String url){
    
    String json_text = "";
    try{
      URL json_url = new URL(url);
      
      Logger.info("URL: " + url);
      
      InputStream inputstream = json_url.openStream();
      InputStreamReader is_reader = new InputStreamReader(inputstream, Charset.forName("UTF-8"));
      BufferedReader b_reader = new BufferedReader(is_reader);
      
      json_text = readerToString(b_reader);
      
    } catch (MalformedURLException e){
      e.printStackTrace();
    } catch (IOException e){
      e.printStackTrace();
    }
    
    
    return json_text; 
  }
  
  private static String readerToString(Reader reader) {
      StringBuilder builder = new StringBuilder();
      
      try{
        int i = 0;
        while ((i = reader.read()) != -1) {
          builder.append((char) i);
        }
      } catch (IOException e){
        e.printStackTrace();
      }
      
      return builder.toString();
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
    if (!com.owner.equals(user()))
      forbidden();
    com.delete();
    
    showLinkedVideo(vid.id);
    
  }
  
  public static void removeLinkedVideo(long video_id){
    LinkedVideo vid = LinkedVideo.findById(video_id);
    User owner = vid.owner;
    if (!owner.equals(user()))
      forbidden();
    List<Comment> comments = vid.comments();
    
    for(int i = 0; i < comments.size(); i++){
      Comment comment = comments.remove(i);
      comment.delete();
    }
    vid.delete();
    
    listLinkedVideos(owner.id);
  }
  
    
  
}
