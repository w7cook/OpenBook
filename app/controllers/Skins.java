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
  public static void editSkin(Map <String, String[]> update)
  {
    User user = user();
    Skin currentUserSkin = user.profile.skin;
    
    if(user.profile.skin.name != user.email){//each user gets a unique skin
      Skin newSkin = new Skin(user.email);
      newSkin.cloneSkin(currentUserSkin);
      user.profile.skin = newSkin;
    }
    System.out.println(update.toString());
    String updateValue;
    
   
    /*
   // Map <String,String[]> update = params.data;
    Set <String> updateKeys = update.keySet();
    //go through each parameter in update
    for(String key: updateKeys)
    {
      updateValue = update.get(key)[0];//should be the value
      
       System.out.println("key: " +key + " value: " + updateValue );
      if(given(updateValue))//if the key value mapped is not empty
      {
        user.profile.skin.setParam(key, updateValue);
      }
    }
    
    //save changes  
    user.profile.save();
    */
    skin(user.id);//rerender the page
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