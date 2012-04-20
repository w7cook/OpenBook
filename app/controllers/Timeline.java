package controllers;

import java.util.*;

import java.util.Date;
import models.TimelineModel;
import models.Post;
import models.User;


public class Timeline extends OBController {
	
	  public static void Timeline(Long userId) {
    User user =  userId == null ? user() : (User) User.findById(userId);
    User currentUser = user();
    render(currentUser, user);
  }

}

	
