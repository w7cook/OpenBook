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
		render("Pages/myPage.html", page,user, currentUser);
	}
	
	public static void pageUpdate(String pid, String info){
		Page page = Page.findById(Long.parseLong(HTML.htmlEscape(pid))); 
		page.info = HTML.htmlEscape(info);
		page.save();
		User user = user();
		User currentUser = user();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("_user", user);
		m.put("_currentUser", currentUser);
		m.put("page", page);
		renderTemplate(m);
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
		boolean fan = isFan(id);
		List<UserPage> myPages = UserPage.find("select u from UserPage u where u.fan = ?", _user).fetch();
		render("Pages/myPage.html", page, fan, _user, _currentUser);
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
	
	public static void unfan(String pid){
		User user = user();
		Page page = Page.findById(Long.parseLong(HTML.htmlEscape(pid)));
		UserPage u = UserPage.find("select u from UserPage u where u.page = ?", page).first();
    u.delete();
		Map<String, Object> m = new HashMap<String, Object>();
    m.put("fan",false);
    renderJSON(m);
	}
	
	public static void fan(String pid){
		User user = user();
		Page page = Page.findById(Long.parseLong(HTML.htmlEscape(pid)));
		final UserPage u = new UserPage(user, page).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("fan",true);
    renderJSON(m);
	}
	
	public static boolean isFan(Long id){
		User user = user();
		UserPage u = UserPage.find("select u from UserPage u where u.page.id = ? and u.fan = ?", id, user).first();
		if(u == null){return false;}
		else{return true;}
	}
}
