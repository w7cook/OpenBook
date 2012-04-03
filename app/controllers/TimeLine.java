package controllers;

import java.util.*;

public class  TimeLine extends OBController {

  public void newsfeed(User user){
    List<Status> timeline = Status.all();
    render(timeline);
  } 

  public void timeline(User user){
    render(null);
  }

  public void updateStatus(...){
    render(null);
  } 
  
  
}
