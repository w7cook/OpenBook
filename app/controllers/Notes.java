package controllers;

import models.User;
import play.mvc.*;

/*
 * Author: Andy Eskridge
 * email: andy.eskridge@gmail.com
 */

@With(Secure.class)
public class Notes extends OBController {

    public static void viewNotes() {
    	User user = user();
        render(user);
    }
    
    public static void newNote() {
    	User user = user();
        render(user);
    }
    
    public static void saveNote() {
    	User user = user();
        render(user);
    }

}
