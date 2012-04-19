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

  public static void createMessage(String username, String subject, String content) {
    User user = user();
    User recipient = User.getUser(username);
    Message message = new Message(user, recipient, subject, content);
    message.save();
    render(user, message);
  }
}
