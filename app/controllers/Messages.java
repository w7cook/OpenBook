package controllers;

import java.util.*;

import controllers.Secure;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import controllers.Secure;
import models.*;

public class Messages extends OBController {

  public static void inbox() {
    User user = user();
    render(user);
  }
  
  public static void sendMessage(User author, User recipient, String title, String content) {
    Message m = new Message(author, recipient, title, content);
    
  }

}
