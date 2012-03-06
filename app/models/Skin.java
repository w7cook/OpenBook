package models;

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

  //body (of the page)
  public String bodyBGColor;

  //logo
  public int logoFontSize;// >= 0
  public String logoColor;//white, black, etc
  public String logoFontType;// Helvetica, Arial, Sans !important;

  //header
  public String headerBGColor;//000000 (BLACK)

  //footer
  public String footerTextAlign;//center..
  public int footerFontSize;// >= 0
  public String footerColor;//white, black, gray, etc


  //section
  public String sectionAlign;//top

  //label
  public int labelFontSize;
  public String labelColor;

  //comment
  public String commentFontSize;
  public String commentColor;
  public int commentBorderSize;//px size
  public String commentBorderColor;
  public String commentBGColor;

  //button
  public int buttonBorderRadius;//4
  public int buttonBorderSize;//1
  public String buttonBorderColor;//black
  public String buttonBoxShadowColor;//888888;
  public String buttonBGColor;//white;
  public String buttonTextDec;//none;
  public String buttonLinkUnvisitedColor;//black    
  public String buttonLinkVisitedColor;//black  
  public String buttonLinkHoverColor;//#E0E0FF;} 
  public String buttonLinkSelectedColor;//black;} 



  public Skin(String name)
  {
    this.name = name;

    //body
    this.bodyBGColor = "none";

    //logo
    this.logoFontSize = 30;
    this.logoColor = "white";
    this.logoFontType = "helvetica";// Helvetica, Arial, Sans !important;

    //header
    this.headerBGColor = "CC5500";//000000 (BLACK) or CC5500 (orange)

    //footer
    this.footerTextAlign = "center";//center..
    this.footerFontSize = 10;// >= 0
    this.footerColor = "gray";//white, black, gray, etc

    //section
    this.sectionAlign = "top";//top

    //label
    this.labelFontSize = 10;
    this.labelColor = "black";

    //comment
    this.commentBorderSize = 2;//px size
    this.commentBorderColor = "white";
    this.commentBGColor = "EEEEEE";


    //button
    this.buttonBorderRadius = 4;//4
    this.buttonBorderSize = 1;//1
    this.buttonBorderColor = "black";//black
    this.buttonBoxShadowColor = "888888";//888888;
    this.buttonBGColor = "white";//white;
    this.buttonTextDec = "none";//none, underline, overline, line-through, blink
    this.buttonLinkUnvisitedColor = "black";//black   
    this.buttonLinkVisitedColor = "black";//black 
    this.buttonLinkHoverColor = "E0E0FF";//#E0E0FF;} 
    this.buttonLinkSelectedColor = "black";//black;}

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
    this.bodyBGColor = c.bodyBGColor;

    //logo
    this.logoFontSize = c.logoFontSize;
    this.logoColor = c.logoColor;
    this.logoFontType = c.logoFontType;// Helvetica, Arial, Sans !important;

    //header
    this.headerBGColor = c.headerBGColor;//000000 (BLACK) or CC5500 (orange)

    //footer
    this.footerTextAlign = c.footerTextAlign;//center..
    this.footerFontSize = c.footerFontSize;// >= 0
    this.footerColor = c.footerColor;//white, black, gray, etc

    //section
    this.sectionAlign = c.sectionAlign;//top

    //label
    this.labelFontSize = c.labelFontSize;
    this.labelColor = c.labelColor;
    //comment

    this.commentBorderSize = c.commentBorderSize;//px size
    this.commentBorderColor = c.commentBorderColor;
    this.commentBGColor = c.commentBGColor;


    //button
    this.buttonBorderRadius = c.buttonBorderRadius;//4
    this.buttonBorderSize = c.buttonBorderSize;//1
    this.buttonBorderColor = c.buttonBorderColor;//black
    this.buttonBoxShadowColor = c.buttonBoxShadowColor;//888888;
    this.buttonBGColor = c.buttonBGColor;//white;
    this.buttonTextDec = c.buttonTextDec;//none, underline, overline, line-through, blink
    this.buttonLinkUnvisitedColor = c.buttonLinkUnvisitedColor;//black   
    this.buttonLinkVisitedColor = c.buttonLinkVisitedColor;//black 
    this.buttonLinkHoverColor = c.buttonLinkHoverColor;//#E0E0FF;} 
    this.buttonLinkSelectedColor = c.buttonLinkSelectedColor;//black;}
  }





}
