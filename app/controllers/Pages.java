package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Pages extends OBController {
	
	public static void newPage(){
		User user = user();
		render(user);
	}
	
	public static void pageSave(String title, String info){
		User user = user();
		User currentUser = user();
		Page page = new Page(user, title, info).save();
		new UserPage(user, page).save();
		//renderText(page.admin+"\n"+page.title+"\n"+page.info+"\n"+pageLink.page.title+"\n"+pageLink.fan);
		render("Pages/myPage.html", page,user, currentUser);
	}
	
	public static void pageUpdate(Long id, String info){
		Page page = Page.findById(id); 
		page.info = info;
		page.save();
		User _user = user();
		User _currentUser = user();
		render("Pages/myPage.html", page, _user, _currentUser);
	}
	
	public static void myPages(){
		User _user = user(); 
		List<UserPage> myPages = UserPage.find("select u from UserPage u where u.fan = ?", _user).fetch();
		if(myPages == null){renderText("null");}
		render(myPages, _user);
	}
	
	public static void display(Long id){
		User _user = user();
		User _currentUser = user();
		Page page = Page.findById(id);
		UserPage pageLink = UserPage.find("select u from UserPage u where u.fan = ? and u.page = ?", _user, page).first();
		String temp = "";
		List<UserPage> pageTest = UserPage.findAll();
		for(UserPage c : pageTest){
			temp+= c.page.title +" -- "+c.fan+"\n";
		}
		render("Pages/myPage.html", page, pageLink, _user, _currentUser);
	}

	public static void pages(){
		User user = user();
		List<Page> allPages = Page.findAll();
		render(allPages,user);
	}
	
	public static void deletePage(Long id){
		User user = user();
		Page page = Page.findById(id);
		UserPage link = UserPage.find("select u from UserPage u where u.page = ?", page).first();
		link.delete();
		page.delete();
		render(user);
	}
	
	public static void unfan(Long id){
		Page page = Page.findById(id);
		User user = user();
		UserPage fanPage = UserPage.find("select u from UserPage u where u.fan  = ? and u.page = ?", user, page).first();
		fanPage.delete();
		display(id);
	}
	
	public static void fan(Long id){
		User user = user();
		Page page = Page.find("select p from Page p where p.id = ?", id).first();
		new UserPage(user, page).save();
		display(id);
	}
	
}
