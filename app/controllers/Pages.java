package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
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
		Page page = new Page(user, title, info).save();
		UserPage pageLink = new UserPage(user, page).save();
		//renderText(page.admin+"\n"+page.title+"\n"+page.info+"\n"+pageLink.page.title+"\n"+pageLink.fan);
		render("Pages/myPage.html", page,user);
	}
	
	public static void pageUpdate(Long id, String info){
		Page page = Page.findById(id); 
		page.info = info;
		page.save();
		User user = user();
		render("Pages/myPage.html", page, user);
	}
	
	public static void myPages(){
		User user = user(); 
		List<UserPage> myPages = UserPage.find("select u from UserPage u where u.fan = ?", user).fetch();
		render(myPages, user);
	}
	
	public static void display(Long id, Post post){
		User user = user();
		Page page = Page.findById(id);
		UserPage pageLink = UserPage.find("select u from UserPage u where u.fan = ? and u.page = ?", user,page).first();
		render("Pages/myPage.html", page, pageLink, user, post);
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
	
	public static void post(String title, String content){
		Page page = Page.find("select p from Page p where p.title = ?", title).first();
		User user = user();
		if(page == null){renderText("null page");}
		//TODO: implement null/empty string check 
		Post p = new Post(user,title,content).save();
		display(page.id, p);
	}

}
