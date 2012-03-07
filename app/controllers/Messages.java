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
  
  public static void createMessage() {
    User user = user();
    render(user);
  }
  
  public static void sendMessage(String recipientUsername, String title, String content) {
    User author = user();
    User recipient = User.getUser(recipientUsername);
    Message m = new Message(author, recipient, title, content);
    m.save();
    inbox();
  }
  

}
