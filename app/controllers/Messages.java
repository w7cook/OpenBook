package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
  
  public static void message(Long messageId) {
    Message item = Message.findById(messageId);
    if(item == null) {
      notFound("That message does not exist.");
    }
    User user = user();
    render(user, item);
  }
  
  public static void createMessage() {
    User user = user();
    render(user);
  }
  
  public static void sendMessage(@Required(message="Recipient is required") String recipientName, String content) {
    User author = user();
    User recipient = User.find("byName", recipientName).first();
    validation.isTrue(recipient != null).message("Invalid Recipient");
    if(validation.hasErrors()){
      renderTemplate("Messages/createMessage.html", author);
    }
    else{
      Message m = new Message(author, recipient, content);
      m.save();
      inbox();
    }
  }
  
  
  

}
