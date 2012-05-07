package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;
import play.data.validation.*;

@With(Secure.class)
public class Statuses extends OBController {

  public static void show(Long id){
    Status status = Status.findById(id);
    User author = User.findById(id);
    render(status, author);
  }

  public static void postStatus(
                                @Required(message="A message is required") String content)
  {

    new Status(Application.user(), content).save();
    Application.news(Application.user().id);
  }

  public List<Status> returnAll(){
    return Status.findAll();
  }
}
