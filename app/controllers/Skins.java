package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Skins extends Controller {
  public static Skin defaultSkin;
  
  @Before
  static void addDefaults() {
    defaultSkin = Skin.find("name = ?", "DEFAULT").first();
   
  }

  
  public static void stylesheet() {
    User user = Application.user(); 
    Skin skin = user.skin;
    renderTemplate("stylesheets/main.css",skin);
  }
  
  /**
   * setSkin
   * @param calling client who wants this skin
   * @return Skin that the client wanted or default if it hasn't been created yet
   */
  public static Skin getSkin(String skinName)
  {
    //find skin
    Skin mySkin = Skin.find("name = ?", skinName).first();
    
    if(mySkin == null)//name hasn't been added so skin doesn't exist
      mySkin = Skin.find("name = ?","DEFAULT").first();//get default String should be set by addDefaults
        
    
    return mySkin;
    
    
    
    
    /*
     
  //  if(isClient == null)// not yet a client so add to list of clients
    //  mySkin.addClient(client);
    
    if(client.skin != null)//need to remove client from previous skin if it existed
      client.skin.removeClient(client);
    
    //set mySkin as the new skin for the client
    client.skin = mySkin;
    client.save();//save change
       */
  }
}