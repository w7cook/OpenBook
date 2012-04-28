package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;
import models.*;

import java.util.*;

import javax.persistence.*;

public class FThreads extends OBController {

  @OneToMany(mappedBy = "fthread", cascade = CascadeType.ALL)
  public List<Comment> allComments;
  
  public static void listSingle(Long threadId) {
    FThread thread = FThread.findById(threadId);
    User _user = Application.user();
    render(thread, _user);
  }
  
  public static void newThread(Long categoryID) {
    User _user = Application.user();
    render(_user, categoryID);
  }
}
