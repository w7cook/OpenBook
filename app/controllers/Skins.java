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
  
  private static boolean given(int val){
    return Integer.toString(val) != null && Integer.toString(val).length() > 0;
  }

  /**
   * edit_skin
   * changes attributes of the skin
   * If there are no changes to the attribute, the skin doesn't change.
   * If there are changes, changes appropriately
   * If there are bad changed, goes to default null.
   */
  public static void editSkin(Skin update)
  {
    User user = user();
    Skin currentUserSkin = user.profile.skin;
    
    if(user.profile.skin.name != user.email){//each user gets a unique skin
      Skin newSkin = new Skin(user.email);
      newSkin.cloneSkin(currentUserSkin);
      user.profile.skin = newSkin;
    }
    
    //body
    if (given(update.bodyBGColor)) { user.profile.skin.bodyBGColor = update.bodyBGColor; }
    
    //logo
    if (given(update.logoFontSize)) { user.profile.skin.logoFontSize = update.logoFontSize; }
    if (given(update.logoColor)) { user.profile.skin.logoColor = update.logoColor; }
    if (given(update.logoFontType)) { user.profile.skin.logoFontType = update.logoFontType; }
    
    //header
    if (given(update.headerBGColor)) { user.profile.skin.headerBGColor = update.headerBGColor; }
    
    //footer
    if (given(update.footerTextAlign)) { user.profile.skin.footerTextAlign = update.footerTextAlign; }
    if (given(update.footerFontSize)) { user.profile.skin.footerFontSize = update.footerFontSize; }
    if (given(update.footerColor)) { user.profile.skin.footerColor = update.footerColor; }
    
    //section
    if (given(update.sectionAlign)) { user.profile.skin.sectionAlign = update.sectionAlign; }
    
    //label
    if (given(update.labelFontSize)) { user.profile.skin.labelFontSize = update.labelFontSize; }
    if (given(update.labelColor)) { user.profile.skin.labelColor = update.labelColor; }
   
    //comment
    if (given(update.commentBorderSize)) { user.profile.skin.commentBorderSize = update.commentBorderSize; }
    if (given(update.commentBorderColor)) { user.profile.skin.commentBorderColor = update.commentBorderColor; }
    if (given(update.commentBGColor)) { user.profile.skin.commentBGColor = update.commentBGColor; }
   
    //button
    if (given(update.buttonBorderRadius)) { user.profile.skin.buttonBorderRadius = update.buttonBorderRadius; }
    if (given(update.buttonBorderSize)) { user.profile.skin.buttonBorderSize = update.buttonBorderSize; }
    if (given(update.buttonBorderColor)) { user.profile.skin.buttonBorderColor = update.buttonBorderColor; }
    if (given(update.buttonBoxShadowColor)) { user.profile.skin.buttonBoxShadowColor = update.buttonBoxShadowColor; }
    if (given(update.buttonBGColor)) { user.profile.skin.buttonBGColor = update.buttonBGColor; }
    if (given(update.buttonTextDec)) { user.profile.skin.buttonTextDec = update.buttonTextDec; }
    if (given(update.buttonLinkUnvisitedColor)) { user.profile.skin.buttonLinkUnvisitedColor = update.buttonLinkUnvisitedColor; }
    if (given(update.buttonLinkVisitedColor)) { user.profile.skin.buttonLinkVisitedColor = update.buttonLinkVisitedColor; }
    if (given(update.buttonLinkHoverColor)) { user.profile.skin.buttonLinkHoverColor = update.buttonLinkHoverColor; }
    if (given(update.buttonLinkSelectedColor)) { user.profile.skin.buttonLinkSelectedColor = update.buttonLinkSelectedColor; }
    //save changes
    user.profile.skin.save();  
    user.profile.save();
    user.save();
    
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
      return false;
    else
    {
      profile.skin = changeSkin;
      profile.save();//made a change in the database so need to save it
      return true;
    }
  }

}