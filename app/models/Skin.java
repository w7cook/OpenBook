package models;

import java.lang.reflect.Field;
import java.util.*;
import javax.persistence.*;
import controllers.Security;
import play.db.jpa.*;
import utils.Bootstrap;


/**
 * Skin
 * @author Lauren Yew
 * 
 * This is the Database model for StyleSheets
 * it is similar to the Firefox personas
 * it uses the template OpenBook/public/stylesheets/main.css
 * which is called by OpenBook/app/views/tags/main.html
 * 
 * Each Profile has one Skin. A skin can have multiple Profiles using it.
 * The attributes of a skin are in its parameters 
 * Which are mapped <SkinPair> values
 * 
 * Differences in Skins are in how they use the template main.css
 * Variables will include: 
 * color
 * size
 * shape
 * (more to be added)
 * 
 * From discussion with Professor: No other templates will be added. 
 * The only changes in Skin will be to variables within main.css
 * 
 * 
 * For colors in HTML: look at http://www.w3schools.com/html/html_colornames.asp
 * 
 */
@Entity
public class Skin extends Model {

  public String userName;//creator's UserName
  public String skinName;//the skin's name
  
  public String isPublic;//whether or not this skin is public
  
  @OneToMany(mappedBy = "attachedSkin", cascade = CascadeType.ALL)
  public List<SkinPair> parameters; // attributes of the skin

  /**
   * addParam (helper method for constructor
   */
  private void addParam(String key, String value)
  {
    SkinPair param = new SkinPair(this,key,value).save();
    this.parameters.add(param);
  }
  
  public Skin(String uN, String sN)
  {
    this.userName = uN;
    this.skinName = sN;
    parameters = new ArrayList<SkinPair>();
    isPublic = "false";
    this.save();
  }


  /**
   * cloneSkin
   * @param c Skin to clone
   * clones attributes from skin c to this Skin
   */
  public void cloneSkin(Skin c)
  {
    //body
    List <SkinPair> parameters = SkinPair.find("attachedSkin = ?",c).fetch();
    for(SkinPair update : parameters)
    {
      this.addParam(update.name,update.value);
    }
  }

  /**
   * get
   * @param String key
   * @return value associated to key in parameters
   */
  public String get(String key)
  {    
    SkinPair param =  SkinPair.find("attachedSkin = ? AND name = ?",this,key).first();
    if(param != null)
      return param.value;
    else 
      return null;
  }

  /**
   * setParam
   * @param key (name of param to change)
   * @param value (updated value)
   * Change param to update value. 
   * NOTE: if value is updated to a bad value, will get this value,
   * but the .css file will not interpret this value, and will give a default of null
   * for the attribute the value is attached to.
   */
  public void setParam(String key, String value)
  {  
       SkinPair s = SkinPair.find("attachedSkin = ? AND name = ?", this, key).first();
       
       if(s == null)
       {
         //s is null so it doesn't exist in the skin, so we make it
          addParam(key,""); 
          s = SkinPair.find("attachedSkin = ? AND name = ?", this, key).first();
       }
       
       if(!value.equalsIgnoreCase(s.value)){
         
         
         
         //take out photo so the color can be seen only if color is changed
         if(key.equals("headerBGColor") &&
             !value.equalsIgnoreCase("none") && 
             !value.equalsIgnoreCase("FFFFFF") &&
             !value.equalsIgnoreCase("white"))//don't want white
         {
           SkinPair headerBGPhoto = SkinPair.find("attachedSkin = ? AND name = ?", this, "headerBGPhoto").first();
           if(headerBGPhoto == null)
           {
             addParam("headerBGPhoto","none");
             headerBGPhoto = SkinPair.find("attachedSkin = ? AND name = ?", this, "headerBGPhoto").first();
           }
           headerBGPhoto.value = "none";
           headerBGPhoto.save();
         }
         
           

         s.value = value;
         s.save();
       }
       
       
  }



}
