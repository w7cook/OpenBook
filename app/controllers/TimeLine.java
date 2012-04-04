package controllers;

import java.util.*;
import play.db.jpa.*;
import models.*;

public class  TimeLine extends OBController {

  public List<Status> news(){
    List<Status> all = Status.all().fetch();
    return all;
    //User user = User.findById(id);
    //return Status.find(
    //          "SELECT s FROM Status s, IN(s.author.subscribers) u WHERE u.subscriber.id = ?1 and p.postType = ?2 order by p.updatedAt desc",
    //          user.id).fetch();
  }

  public void updateStatus(String content){
    Status newStatus = new Status(OBController.user(), content);
    newStatus.save();
    render(null);
  } 
  
  public void addPhoto(Blob image, String content){
    Status newStatus = new Photo(OBController.user(), image, content);
    newStatus.save();
    render(null);
  }
  
  public void askQuestion(){
    render(null);
  }
  
}
