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
 */
@Entity
public class Skin extends Model {

	public String name;//name of the skin
	
	//logo
	public int logoFontSize;// >= 0
	public String logoFontItalic;//italic
	
	public String logoColor;//white, black, etc
	/*
	public String logoFontBold;//bold
	
	public String logoFontType;// Helvetica, Arial, Sans !important;
	*/
	//header
	public String headerBGColor;//000000 (BLACK)
	/*
	//footer
	public String footerTextAlign;//center..
	public int footerFontSize;// >= 0
	public String footerColor;//white, black, gray, etc
	public String footerFontBold;//bold
	public String footerFontItalic;//Italic
	
	
	//section
	public String sectionAlign;//top
	
	//label
	public String labelFontSize;
	public String labelColor;
	public String labelFontBold;
	public String labelFontItalic;
	
	//comment
  public String commentFontSize;
  public String commentColor;
  public String commentFontBold;
  public String commentFontItalic;
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
  public String buttonLinkVicistedColor;//black  
  public String buttonLinkHoverColor;//#E0E0FF;} 
  public String buttonLinkSelectedColor;//black;} 
  */
  

	public Skin(String name)
	{
		this.name = name;
	
		//logo
		this.logoFontSize = 30;
		
		this.logoFontItalic = "";//" italic"
		
		this.logoColor = "white";
		/*
		this.logoFontBold = "bold;";//? 
		this.logoFontType = "helvetica";// Helvetica, Arial, Sans !important;
		*/
		//header
		this.headerBGColor = "CC5500";//000000 (BLACK) or CC5500 (orange)
		/*
		//footer
		this.footerTextAlign = "center";//center..
		this.footerFontSize = 10;// >= 0
		this.footerFontItalic = " italic";//Italic
		this.footerColor = "gray";//white, black, gray, etc
		
		
		this.footerFontBold;//bold
		
		
		
		//section
		this.sectionAlign;//top
		
		//label
		this.labelFontSize;
		this.labelColor;
		this.labelFontBold;
		this.labelFontItalic;
		
		//comment
	  this.commentFontSize;
	  this.commentColor;
	  this.commentFontBold;
	  this.commentFontItalic;
	  this.commentBorderSize;//px size
	  this.commentBorderColor;
	  this.commentBGColor;
	  
	  //button
	  this.buttonBorderRadius;//4
	  this.buttonBorderSize;//1
	  this.buttonBorderColor;//black
	  this.buttonBoxShadowColor;//888888;
	  this.buttonBGColor;//white;
	  this.buttonTextDec;//none;
	  this.buttonLinkUnvisitedColor;//black   
	  this.buttonLinkVicistedColor;//black 
	  this.buttonLinkHoverColor;//#E0E0FF;} 
	  this.buttonLinkSelectedColor;//black;}
	  */
	  
	  
		
		this.save();
	}
	

	
	public void setFont()
	{
		
	}
	
	
		
}
