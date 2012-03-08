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
 * it uses the template OpenBook/app/views/stylesheets/main.css
 * which is called by OpenBook/app/views/tags/main.html
 * 
 * Each User has one Skin. Each Skin can have multiple users.
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
 * We have some built in Skins in initial-data.yml. Note: if the Skin field is not 
 * set to something in the initial-data.yml, that field is set to null.
 * 
 *
 */
@Entity
public class Skin extends Model {

  public String name;//name of the skin

  @OneToMany(mappedBy = "attachedSkin", cascade = CascadeType.ALL)
  public List<SkinPair> parameters; // attributes of the skin

  public Skin(String name)
  {
    this.name = name;
    parameters = new ArrayList<SkinPair>();
    this.save();

    SkinPair param = new SkinPair(this,"bodyBGColor","blue").save();
    
    //body
    parameters.add(param);
/*
    //logo
    parameters.put("logoFontSize", "30");
    parameters.put("logoColor","white");
    parameters.put("logoFontType","helvetica");// Helvetica, Arial, Sans !important;

    //header
    parameters.put("headerBGColor","CC5500");//000000 (BLACK) or CC5500 (orange)

    //footer
    parameters.put("footerTextAlign","center");//center..
    parameters.put("footerFontSize","10");// >= 0
    parameters.put("footerColor","gray");//white, black, gray, etc

    //section
    parameters.put("sectionAlign","top");//top

    //label
    parameters.put("labelFontSize","10");
    parameters.put("labelColor","black");

    //comment
    parameters.put("commentBorderSize","2");//px size
    parameters.put("commentBorderColor","white");
    parameters.put("commentBGColor","EEEEEE");


    //button
    parameters.put("buttonBorderRadius","4");//4
    parameters.put("buttonBorderSize","1");//1
    parameters.put("buttonBorderColor","black");//black
    parameters.put("buttonBoxShadowColor","888888");//888888;
    parameters.put("buttonBGColor","white");//white;
    parameters.put("buttonTextDec","none");//none, underline, overline, line-through, blink
    parameters.put("buttonLinkUnvisitedColor","black");//black   
    parameters.put("buttonLinkVisitedColor","black");//black 
    parameters.put("buttonLinkHoverColor","E0E0FF");//#E0E0FF;} 
    parameters.put("buttonLinkSelectedColor","black");//black;}
  */  
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
   * Change param to update value
   */
  public void setParam(String key, String value)
  {
       SkinPair s = SkinPair.find("attachedSkin = ? AND name = ?", this, key).first();
       if(s != null){
         s.value = value;
         s.save();
         this.save();
       }
  }



}
