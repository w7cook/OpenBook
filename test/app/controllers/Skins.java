package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;
import controllers.Photos;

@With(Secure.class)
public class Skins extends OBController {
  public static List<Skin> skins() {
    List<Skin> skinList = Skin.find("isPublic = ?", "true").fetch();
    return skinList;
  }

  public static void sampleSkin(Long skinId)
  {
    User user = user();
    Skin skin;
    if(skinId == null)
    {
        skin = Skin.find("skinName = ?","default_skin").first();//get default skin
    }
    else
    {
       skin = Skin.findById(skinId);
    }
    render(user,skin);
  }
  /**
   * stylesheet()
   * called by main.html
   * renders the appropriate stylesheet skin for the current user
   */
  public static void stylesheet() {
    User user = user();
    Skin skin = user.profile.skin;
    renderTemplate("/public/stylesheets/main.css",skin);
  }

  /**
   * renders change skin page
   */
  public static void changeSkin(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    List<Skin> skinList = skins();
    render(user, skinList);
  }
  
  /**
   * editMySkin
   * renders edit skin page
   * if we are editing the skin, then we need to create a new skin so we can only edit our own skin
   */
  public static void editMySkin(Long id,String makeSkinOutput) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user, makeSkinOutput);
  }

  /**
   * given helper method --tells whether or not the form has any input
   * @param val String
   * @return
   */
  private static boolean given(String val) {
    return val != null && val.length() > 0;
  }

  /**
   * edit_skin
   * changes attributes of the skin
   * If there are no changes to the attribute, the skin doesn't change.
   * If there are changes, changes appropriately
   * If there are bad changed, goes to default null.
   */
  public static void editSkin(String key, String val)
  {
    User user = user();
    Skin currentUserSkin = user.profile.skin;
    System.out.println("val: " + val);
    if(!val.contains(";") && !val.contains("."))
    {//checks for SQL injection
      if(user.profile.skin.userName != user.username){//each user gets a unique skin
        Skin newSkin = new Skin(user.username,"mySkin");//make a skin for that user
        newSkin.cloneSkin(currentUserSkin);
        user.profile.skin = newSkin;
        user.profile.save();
      }
      
      if(key != null){//null if theres no changes
        
        String[] keys = key.split(", ");//input is a list
        String[] values = val.split(", ");
        String keyUpdate = "";
        String valueUpdate = "";
        
        //go through the attributes
        for(int x = 0; x< keys.length; x++)
        {
          keyUpdate = keys[x];
          valueUpdate = values[x];
          
          if(given(valueUpdate))//if the attribute has been filled, set parameter
          {
            user.profile.skin.setParam(keyUpdate,valueUpdate);
            
          }
        }
    }
     
      //save changes  
      user.profile.save();
     
     
    }
    editMySkin(user.id,null);//rerender the page for current user (input null will find user();
  }

  /**
   * changes Skin to template skin
   * just sets our parameters be the same
   * used so that can redirect later and won't change if skin name is not there
   */
  public static void changeSkinToTemplate(String skinName)
  {
    //find skin
    User user = user();
    Skin changeSkin = Skin.find("userName = ? AND skinName = ?", "default", skinName).first();
    if(changeSkin != null)
    {
     user.profile.skin = changeSkin;
     user.profile.save();
    }
    changeSkin(user.id);//rerender page
  }
  
  public static void setBackgroundPhoto(Long photoid)
  {
    User user = user();
    Skin changeSkin = user.profile.skin;
    if(user.profile.skin.userName != user.username){//each user gets a unique skin
      Skin newSkin = new Skin(user.username,"mySkin");//make a skin for that user
      newSkin.cloneSkin(changeSkin);
      user.profile.skin = newSkin;
      user.profile.save();
      changeSkin = user.profile.skin;
    }
    
    SkinPair update = SkinPair.find("attachedSkin = ? AND name = ?", changeSkin, "bodyBGPhoto").first();
    if(update != null)
    {
      update.value = "/photos/" + photoid.toString();
      update.save();
    }
    Photos.photos(user.id);
  }
  
  public static Skin getSkin(String userName, String skinName)
  {
    return Skin.find("userName = ? AND skinName = ?",userName, skinName).first();
  }
  
  public static void makeSkin(String skinName)
  {
    User user = user();
    String makeSkinOutput = "";

    if(!user.profile.skin.userName.equals(user.username))
    {
      makeSkinOutput = "You cannot make a new Skin from a template.";
    }
    else
    {
      //security check
      if(!skinName.contains(";") && !skinName.contains("."))
      {
        //we want anyone to be able to use this skin, so it can't be allowed to change
          Skin s2 = Skin.find("userName = ? AND skinName = ?", "default", skinName).first();
          if(s2!=null)
            makeSkinOutput = ("Skin Name has already been used in the templates." +
                " Please specify another skin name.");
          else{
            Skin newSkin = new Skin("default",skinName);//make a template skin of that user
            newSkin.cloneSkin(user.profile.skin);
            newSkin.isPublic = "true";
            newSkin.save();
            //change the profile's skin
            user.profile.skin = newSkin;
            user.profile.save();
            makeSkinOutput = "SUCCESS!";
          }
        
      }
    }
    editMySkin(user.id,makeSkinOutput);//rerender the page for current user (input null will find user();
    
  }

}