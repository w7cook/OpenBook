package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Pages extends OBController {
	
	public static void newPage(){
		User currentUser = user();
		render(currentUser);
	}
	
	public static void pageSave(String title, String info){
		User currentUser = user();
		Page page = new Page(currentUser, title, info).save();
		UserPage pageLink = new UserPage(currentUser, page).save();
		//renderText(page.admin+"\n"+page.title+"\n"+page.info+"\n"+pageLink.page.title+"\n"+pageLink.fan);
		render("Pages/myPage.html", page,currentUser);
	}
	
	public static void pageUpdate(Long id, String info){
		Page page = Page.findById(id); 
		page.info = info;
		page.save();
		User currentUser = user();
		render("Pages/myPage.html", page,currentUser);
	}
	
	public static void myPages(){
		User currentUser = user(); 
		List<UserPage> myPages = UserPage.find("select u from UserPage u where u.fan = ?", currentUser).fetch();
		render(myPages);
	}
	
	public static void display(Long id){
		User currentUser = user();
		Page page = Page.findById(id);
		//UserPage pageLink = UserPage.find("selece u from UserPage u where u.page = ?", page)
		UserPage pageLink = UserPage.findById(id);
		render("Pages/myPage.html", page, pageLink, currentUser);
	}

	public static void pages(){
		List<Page> allPages = Page.findAll();
		render(allPages);
	}
	
	public static void deletePage(Long id){
		Page page = Page.findById(id);
		UserPage link = UserPage.find("select u from UserPage u where u.page = ?", page).first();
		link.delete();
		page.delete();
		render();

	}
}
