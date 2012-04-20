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
  
  public static void sendMessage(@Required(message="Recipient is required") String recipientUsername, String content) {
    User author = user();
    validation.isTrue(author.getUser(recipientUsername)!=null).message("Invalid Recipient");
    if(validation.hasErrors()){
      renderTemplate("Messages/createMessage.html", author);
    }
    else{
      User recipient = User.getUser(recipientUsername);   
      Message m = new Message(author, recipient, content);
      m.save();
      inbox();
    }
  }
  
  
  

}
