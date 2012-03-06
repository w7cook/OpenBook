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
    Skin skin = user.skin;
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

  private static boolean given(String val) {
    return val != null && val.length() > 0;
  }

  /**
   * edit_skin
   * changes attributes of the skin
   */
  public void edit_skin(Skin update)
  {
    User user = user();
    Skin currentUserSkin = user.skin;
    if(user.skin.name != user.email){//each user gets a unique skin
      Skin newSkin = new Skin(user.email);
      newSkin.cloneSkin(currentUserSkin);
      user.skin = newSkin;
    }
    //Update attributes
    if (given(update.bodyBGColor)) {
      user.skin.bodyBGColor = update.bodyBGColor;
    }

    user.skin.save();  
    user.save();
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