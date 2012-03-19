package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Skins extends OBController {
  
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
   * skin
   * renders edit skin page
   * if we are editing the skin, then we need to create a new skin so we can only edit our own skin
   */
  public static void skin(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
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
    
    if(user.profile.skin.name != user.email){//each user gets a unique skin
      Skin newSkin = new Skin(user.email);
      newSkin.cloneSkin(currentUserSkin);
      user.profile.skin = newSkin;
    }
   
    
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
    
   
    //save changes  
    user.profile.save();
   
    skin(null);//rerender the page for current user (input null will find user();
  }



  /**
   * setSkin
   * @param calling client who wants this skin
   * @return true if the skin has been sucessfully set, false otherwise
   * Sets the client's skin to the skin of skin name and returns true
   * If the skin name is not found, does not reset and returns false
   *
   */
  public static boolean setSkin(Profile profile, String skinName)
  {
    //find skin
    Skin changeSkin = Skin.find("name = ?", skinName).first();

    if(changeSkin == null)//name hasn't been added so skin doesn't exist
    {
      profile.skin = new Skin(skinName).save();
      profile.save();
      return true;
    }
    else
    {
      profile.skin = changeSkin;
      profile.save();//made a change in the database so need to save it
      return true;
    }
  }

}