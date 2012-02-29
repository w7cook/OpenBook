package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Page extends Controller { 
	public static UserPage currPage;

  @Before
  static void setConnectedUser() {
    if (Security.isConnected()) {
      renderArgs.put("currentUser", user());
    }
  }
  
  public static User user() {
    assert Secure.Security.connected() != null;
    return User.find("byEmail", Secure.Security.connected()).first();
  }  

 public static void Page(){
  	User currentUser = user();  	
  	UserPage page = new UserPage();
  	//if the current user has no pages
  	if(currentUser.pages.size()==0){		
;
  	}
  	render("Application/account.html", currentUser);
  }
  
  public static void update(UserPage page, String newInfo){
  	User currentUser = user();
  	page.info = newInfo;
  	page.save();
  	renderTemplate("Page/myPage.html", currentUser, page);
  }
  
  public static void newPage(){
  	User currentUser = user(); 
  	UserPage page = new UserPage();
  	renderTemplate("Page/newPage.html", currentUser);
  }

  //link newPage to Page after creating page   
	public static void newSet(String title, String info){
		User currentUser = user();
		UserPage page = new UserPage();
		if(currPage == null){
			page.setData(currentUser, title, info);
			//currPage = page;
			page.save();
			currentUser.pages.add(page);
		}
		else{
			page = findMyPageByTitle(currentUser, title);
			//page.setInfo(info);
		}
		currentUser.save();
		renderTemplate("Page/myPage.html", currentUser, page);
	}
	
	public static void display(Page page){
		User currentuser = user();
		//if(currentuser.id == page.Admin; 
	
	}
	
	public static UserPage findMyPageByTitle(User user, String pageTitle){
		return UserPage.find("admin = ? AND title = ?", user.id, pageTitle).first();
	}
	
}
