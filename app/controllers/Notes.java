package controllers;

import models.Note;
import models.User;
import play.mvc.*;

/*
 * Author: Andy Eskridge
 * email: andy.eskridge@gmail.com
 */

public class Notes extends OBController {

  public static void viewNotes() {
    User user = user();
    render(user);
  }

  public static void newNote() {
    User user = user();
    render(user);
  }

  public static void saveNote(String title, String content) {
    Note n = new Note(user(), title, content);
    n.save();
    viewNotes();
  }
}
