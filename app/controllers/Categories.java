package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;
import models.*;

import java.util.*;

import javax.persistence.*;

@With(Secure.class)
public class Categories extends OBController {
	
	@Before
	static void setConnectedUser() 
	{
		if (Security.isConnected()) 
		{
			renderArgs.put("currentUser", user());
		}
	}
	
	public static User user() {
		assert Secure.Security.connected() != null;
		return User.find("byEmail", Secure.Security.connected()).first();
	}

	public static void listAll()
	{
        List<Category> Categories = Category.find("order by name").fetch();
        render(Categories);
	}
	
	public static void listSingle(Long catId)
	{
		Category category = Category.findById(catId);
		List<FThread> FThreads = FThread.find("category = ? order by postedAt desc", category).fetch(10);
		render(FThreads, category);
  
	}
	

}
