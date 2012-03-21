package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;
import models.*;

import java.util.*;

import javax.persistence.*;

public class FThreads extends Controller {

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

  // public static void listAll(Long catID)
  // {
  //       Category cat = Category.findById(catID);
  //         List<FThread> FThreads = FThread.find("cat = ? order by postedAt desc", cat).fetch(10);
  //         render(FThreads);
  // }
	
	public static void listSingle(Long threadId)
	{
		FThread thread = FThread.findById(threadId);
        render(thread);
	}
	
	@OneToMany(mappedBy = "fthread", cascade = CascadeType.ALL)
	public List<Comment> allComments;

}
