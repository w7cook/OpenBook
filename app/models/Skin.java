package models;

import java.lang.reflect.Field;
import java.util.*;
import javax.persistence.*;
import controllers.Security;
import play.db.jpa.*;


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

  public String name;//name of the skin
  
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
  
  public Skin(String name)
  {
    this.name = name;
    parameters = new ArrayList<SkinPair>();
    this.save();

   
    
    //body
    addParam("bodyBGColor","none");
    
    //logo
    
    addParam("logoFontSize", "30");
    addParam("logoColor","white");
    addParam("logoFontType","helvetica");// Helvetica, Arial, Sans !important;

    //header
    addParam("headerBGColor","CC5500");//000000 (BLACK) or CC5500 (orange)

    //footer
    addParam("footerTextAlign","center");//center..
    addParam("footerFontSize","10");// >= 0
    addParam("footerColor","gray");//white, black, gray, etc

    //section
    addParam("sectionAlign","top");//top

    //label
    addParam("labelFontSize","10");
    addParam("labelColor","black");

    //comment
    addParam("commentBorderSize","2");//px size
    addParam("commentBorderColor","white");
    addParam("commentBGColor","EEEEEE");


    //button
    addParam("buttonBorderRadius","4");//4
    addParam("buttonBorderSize","1");//1
    addParam("buttonBorderColor","black");//black
    addParam("buttonBoxShadowColor","888888");//888888;
    addParam("buttonBGColor","white");//white;
    addParam("buttonTextDec","none");//none, underline, overline, line-through, blink
    addParam("buttonLinkUnvisitedColor","black");//black   
    addParam("buttonLinkVisitedColor","black");//black 
    addParam("buttonLinkHoverColor","E0E0FF");//#E0E0FF;} 
    addParam("buttonLinkSelectedColor","black");//black;}
  
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
    this.parameters = SkinPair.find("attachedSkin = ?",c).fetch();

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
       if(s != null){
         s.value = value;
         s.save();
       }
  }



}
