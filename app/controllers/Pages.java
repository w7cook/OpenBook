package controllers;

import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import play.*;
import play.data.validation.Error;
import play.libs.*;
import play.mvc.*;
import play.db.jpa.*;
import models.*;
import java.security.*;
import java.net.*;
import java.awt.image.*;
import play.utils.HTML;

@With(Secure.class)
public class Pages extends OBController {
	
	public static final String IMAGE_TYPE = "^image/(gif|jpeg|pjpeg|png)$";
  public static final int MAX_FILE_SIZE = 2 * 1024 * 1024;  /* Size in bytes. */
	
	public static void newPage(){
		User user = user();
		render(user);
	}
	
	public static void pageSave(String title, String info){
		User user = user();
		User currentUser = user();
		Page page = new Page(user, title, info).save();
		new UserPage(user, page).save();
		display(page.id);
	}
	

  public static void pageUpdate(Long id, String info){
  	Page page = Page.findById(id); 
  	page.info = info;
    page.save();
    User _user = user();
    display(id);
  }
	                                                                     
	public static void myPages(){
		User _user = user();  
		List<UserPage> myPages = UserPage.find("select u from UserPage u where u.fan = ? and u.page.admin != ?", _user, _user).fetch();
		List<Page> pages = Page.find("select p from Page p where p.admin = ?",_user).fetch();
		if(myPages == null){renderText("null");}
		render(myPages, _user, pages);
	}
	
	public static void display(Long id){
		User _user = user();
		User _currentUser = user();
		Page page = Page.findById(id);
		boolean fan = isFan(id);
		List<UserPage> myPages = UserPage.find("select u from UserPage u where u.fan = ?", _user).fetch();
		List<Photo> photos = page.photos;
		render("Pages/myPage.html", page, fan, _user, _currentUser, photos);
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
		new UserPage(user, page).save();
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
	
	public static void addPhoto(Long id, File image) throws FileNotFoundException,
                                                 IOException {
		Page page = Page.findById(id);
		    validation.keep(); /* Remember any errors after redirect. */

    if (image == null ||
        !MimeTypes.getContentType(image.getName()).matches(IMAGE_TYPE)) {
      validation.addError("image",
                          "validation.image.type");
      redirect("/users/" + user().id + "/photos");
    }
    Photo photo = new Photo(user(), image,"");
    validation.max(photo.image.length(), MAX_FILE_SIZE);

    if (!validation.hasErrors()) {
      photo.save();
      page.photos.add(photo);
      if(page.photos.size() == 1){
      	page.profilePhoto = photo.id;
      }
      page.save();
    }
    display(page.id);
  }
  
  public static void setProfilePic(Long pid, Long photoid){
  	Page p = Page.findById(pid);
  	p.profilePhoto = photoid;
  	p.save();
  	display(pid);
  }
}
