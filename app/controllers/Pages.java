package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;


@With(Secure.class)      
public class Pages extends Application { 
	//list all pages
	public static void pages(){
		List<Page> pageList = Page.findAll(); 
		renderTemplate("Page/pages.html", pageList);
	}
	//newPage view
  public static void newPage(){
  	renderTemplate("Page/myPage.html");
  }

  //link newPage to Page after creating page   
	public static void savePage(String title, String info){
		User currentUser = user();
		//remove leading and trailing whitespaces
		info = info.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
		Page page = findMyPage(currentUser,title);
		if(page != null){
			page.update(info);
			page.save();			
			renderTemplate("Page/myPage.html", page, currentUser);
		}
		renderText("null page error");
	}
	//Makes a new page
	public static void makePage(String title, String info){
		info = info.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
		User currentUser = user();
		Page page = new Page(currentUser, title, info);
		UserPage mypage = new UserPage(page,currentUser);
		page.save();
		mypage.save();
		renderTemplate("Page/myPage.html", page, currentUser);
	}
	//admin ? admin view : fan view;
	public static void display(Page page){
		User currentUser = user();
		int fanFlag;
		if((page.admin).equals(currentUser.email)){
			renderTemplate("Page/myPage.html", page);
		}
		else{
			renderTemplate("Page/page.html",page);
		}
	}                             
	
	public static Page findMyPage(User user, String title){
		Page mypage = Page.find("SELECT u FROM Page u where u.title= ? AND u.admin = ?", title, user.email).first();
		if(mypage != null){return mypage;}
		else{return null;}
	}
	//Fix
	public int isFan(Page p){
		UserPage u = UserPage.find("Select u from UserPage u where u.fan = ? and u.page = ?", user(), p).first();
		renderText(u.page.title);
		if(u == null){return 0;}
		else{return 1;}
	}
	
}
