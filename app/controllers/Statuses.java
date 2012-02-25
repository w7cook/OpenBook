package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Statuses extends Controller {
    
    public static Status show(Long id){
        Status status = Status.findById(id);
        render(status);
    }
    
    public static void postStatus(
        @Required(message="Author is required") String author,
        @Required(message="A message is required") String content) 
    {
        
        Status status = new Status(author, content).save();
        flash.success("Thanks for posting %s", author);
        show(status.id);
    }
}
