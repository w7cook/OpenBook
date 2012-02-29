package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Page extends Application { 
	public static UserPage currPage;

	public static void Page(){
  	User currentUser = user();  	
  	UserPage page = new UserPage();
  	//if the current user has no pages
  	if(currentUser.pages.size()==0){		
;
  	}
  	render("Application/account.html", currentUser);
  }
    
  public static void newPage(){
  	User currentUser = user();
  	UserPage page = new UserPage();
  	renderTemplate("Page/myPage.html", currentUser, page);
  }

  //link newPage to Page after creating page   
	public static void savePage(String title, String info){
		User currentUser = user();
		UserPage page = new UserPage();
		page.setData(currentUser, title, info);
			//currPage = page;
		page.save();
		currentUser.pages.add(page);
		currentUser.save();
		//set url to page title?
		renderTemplate("Page/myPage.html", currentUser, page);
	}
	
	//fix for later doesnt appear to be working right
	public static UserPage findMyPageByTitle(User user, String pageTitle){
		return UserPage.find("SELECT x FROM UserPage x where x.admin = ? AND x.title = ?", user.id, pageTitle).first();
	}
	
}
