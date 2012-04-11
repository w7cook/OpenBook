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
		Page page = new Page(user, title, info).save();
		new UserPage(user, page).save();
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
		if(myPages == null){renderText("null");}
		render(myPages, user);
	}
	
	public static void display(Long id){
		User user = user();
		Page page = Page.findById(id);
		UserPage pageLink = UserPage.find("select u from UserPage u where u.fan = ? and u.page = ?", user,page).first();
		String temp = "";
		List<UserPage> pageTest = UserPage.findAll();
		for(UserPage c : pageTest){
			temp+= c.page.title +" -- "+c.fan+"\n";
		}
		render("Pages/myPage.html", page, pageLink, user);
	}
	/*
	public static void display(Long id, Map m){
		User user = user();
		Page page = Page.findById(id);
		UserPage pageLink = UserPage.find("select u from UserPage u where u.fan = ? and u.page = ?", user,page).first();
		String temp = "";
		List<UserPage> pageTest = UserPage.findAll();
		for(UserPage c : pageTest){
			temp+= c.page.title +" -- "+c.fan+"\n";
		}
		render("Pages/myPage.html", page, pageLink, user, m);
	}
	*/
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
	/*
	public static void post(Long id, String content){
		Page page = Page.find("select p from Page p where p.id = ?", id).first();
		User user = user();
		if(page == null){renderText("null page");}
		//TODO: implement null/empty string check 
		new Post(user,page.id.toString(),content,Post.type.PAGE).save();
		display(page.id);
	}
	
	public static void post(Long id, String postContent){ 
		final Page p = Page.find("select p from Page p where p.id = ?", id).first();
		final User u = user();
		//TODO: implement null/empty string check 
		final Post po = new Post(u,p.id.toString(),HTML.htmlEscape(postContent),Post.type.PAGE).save();
    Map<String, Object> m = new HashMap<String, Object>();
		m.put("item", po);
		m.put("user", user());
		m.put("currentUser", user());
    renderTemplate(m);
	}
	*/
	public static void post(String pid, String content){ 
		final User u = user();
		final Post p = new Post(u,HTML.htmlEscape(pid),HTML.htmlEscape(content),Post.type.PAGE).save();
    Map<String, Object> m = new HashMap<String, Object>();
		m.put("item", p);
		m.put("user", user());
		m.put("currentUser", user());
    renderTemplate(m);
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
