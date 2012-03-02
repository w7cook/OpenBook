package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Skins extends Controller {
  /**
   * stylesheet()
   * called by main.html
   * renders the appropriate stylesheet skin for the current user
   */
  public static void stylesheet() {
    User user = Application.user(); 
    Skin skin = user.skin;
    renderTemplate("stylesheets/main.css",skin);
  }
  
  /**
   * setSkin
   * @param calling client who wants this skin
   * @return true if the skin has been sucessfully set, false otherwise
   * Sets the client's skin to the skin of skin name and returns true
   * If the skin name is not found, does not reset and returns false
   * 
   */
  public static boolean setSkin(User u, String skinName)
  {
    //find skin
    Skin changeSkin = Skin.find("name = ?", skinName).first();
    
    if(changeSkin == null)//name hasn't been added so skin doesn't exist
      return false;
    else
    {
      u.skin = changeSkin;
      u.save();//made a change in the database so need to save it
      return true;
    }
    
  }
}