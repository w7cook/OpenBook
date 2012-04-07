package controllers;

import java.util.*;
import play.db.jpa.*;
import models.*;

public class  Links extends OBController {

  public static void addLink(String url, String content){
    Post newPost = new Link(OBController.user(), content, url);
    newPost.save();
  }
  
}
