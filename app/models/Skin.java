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
	public String logoColor;//white, black, etc
	public String logoFontBold;//bold
	public String logoFontItalic;//italic
	public String logoFontType;// Helvetica, Arial, Sans !important;
	
	//header
	public String headerBGColor;//000000 (BLACK)
	
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
  
	

	public Skin(String name)
	{
		this.name = name;
		this.logoFontSize = 30;
		//this.backgroundColor = "000000";//"CC5500";
		//this.fontType = "Arial"; //Options: Helvetica, Arial, Sans !important
		this.save();
	}
	

	
	public void setFont()
	{
		
	}
	
	
		
}
