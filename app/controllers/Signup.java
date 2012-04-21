package controllers;

import java.io.File;
import java.util.*;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import controllers.Secure;
import models.TempUser;
import models.User;
import play.Play;
import play.cache.Cache;
import play.data.validation.Error;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.IO;
import play.libs.Images;
import play.libs.Mail;
import play.mvc.*;
//import controllers.Secure;

public class Signup extends Controller {

  public static void signup() {
    String randomID = Codec.UUID();
    render(randomID);
  }
  
  public static void captcha(String id) {
    Images.Captcha captcha = Images.captcha();
    String code = captcha.getText("#000");
    Cache.set(id, code, "10mn");
    renderBinary(captcha);
  }
  
  public static void get_new_captcha() {
    String randomID = Codec.UUID();
    renderText(randomID);
  }

  public static void signup_user(String firstName, String lastName, String username, String email, String email2, String password, String password2,
      @Required(message="Please type the code") String code, String randomID) {
    validation.email(email).message("Please use a valid email address");
    validation.isTrue(email.trim().length() >= 1).message("Please use a valid email address");
    validation.equals(email, email2).message("Email addresses do match" + email + email2);
    validation.isTrue(checkUniqueEmail(email)).message("An account is already attached to this email address.  Did you forget your password?");
    validation.isTrue(firstName.trim().length() >= 1).message("Please enter a first name");
    validation.isTrue(lastName.trim().length() >= 1).message("Please enter a last name");
    username = username.trim();
    validation.isTrue(checkUniqueUsername(username)).message("This username is in use");
    validation.isTrue(username.trim().length() >= 4).message("Username must be at least 4 characters");
    validation.isTrue(password.length() >= 6).message("Password must be at least 6 characters");
    validation.match(password, ".*[A-Z]+.*").message("Password must contain an uppercase letter");
    validation.match(password, ".*[a-z]+.*").message("Password must contain a lowercase letter");
    validation.match(password, "(.*\\W+.*)|(.*[0-9]+.*)").message("Password must contain a non-letter character");
    validation.equals(password, password2).message("Passwords do match");
    if(!Play.id.equals("test")) {
      validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
    }
    if (validation.hasErrors()) {
      String errors ="";
      for(Error error : validation.errors()) {
        errors += error.message() + "<br>";
      }
      renderText(errors);
    }
    else {
      try {
        String confirmID = Codec.UUID();
        new TempUser(email, password, username, firstName, lastName, confirmID).save();
        sendConfirmationEmail(email, confirmID);
      } catch (EmailException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      flash.put("success", "Registration almost complete!  Just verify your e-mail address, and you will be able to log in");
      flash.keep();
      renderText("");
    }
  }
  
  /** Sends an email to confirm valid emails and complete registration for a user.
   * 
   * @param emailTo the users email
   * @param confirmID the confirmation id
   * @throws EmailException thrown from the SimpleEmail class
   */
  private static void sendConfirmationEmail(String emailTo, String confirmID) throws EmailException {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", confirmID);
    String url = Router.reverse("Signup.confirm", map).url;
    url = Play.configuration.getProperty("application.baseUrl") + url;
    String password = IO.readContentAsString(new File("conf/passwd.pwd")).trim();
    Play.configuration.setProperty("mail.smtp.pass", password);
    SimpleEmail email = new SimpleEmail();
    email.setFrom("registration@openbook.com");
    email.addTo(emailTo);
    email.setSubject("Welcome to OpenBook! Please confirm your e-mail");
    email.setMsg("Welcome to OpenBook!  We are glad to have you onboard - but we need to confirm your email first.  You can do so by going here: " + url);
    Mail.send(email); 
  }
  
  /** Confirms the email for a given id and creates the user in the database.
   * 
   * @param id the UUID assigned at registration time.
   */
  public static void confirm(String id) {
    TempUser user = TempUser.find("SELECT u FROM TempUser u WHERE u.UUID = ?", id).first();
    if (user.verified == false) {
      new User(user).save();
    }
    flash.put("success", "E-mail verified and registration complete!  Sign in below.");
    flash.keep();
    try {
      Secure.login();
    } catch (Throwable e) {};
  }

  public static void isValidUserName(String name) {
    if (!checkUniqueUsername(name)) {
      renderText("This username has been taken");
    } else if (name.length() < 4) {
      renderText("The username must have at least 4 characters");
    } else {
      renderText("Username available :)");
    }
  }

  private static boolean checkUniqueUsername(String name) {
    if (User.count("username = ?", name) > 0) {
      return false;
    } else {
      return true;
    }
  }

  private static boolean checkUniqueEmail(String email) {
    if (User.count("email = ?", email) > 0) {
      return false;
    } else {
      return true;
    }
  }

  public static void isValidEmail(String email) {
    if (validation.email(email).ok == false || email.trim().length() == 0) {
      renderText("Please use a valid email address");
    }
    else if (!checkUniqueEmail(email)) {
      renderText("An account is already attached to this email address.  Did you forget your password?");
    } else {
      renderText(":)");
    }
  }
}