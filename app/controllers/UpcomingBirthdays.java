package controllers;

import java.util.*;

import org.elasticsearch.index.query.QueryBuilders;

import play.*;
import play.modules.elasticsearch.ElasticSearch;
import play.modules.elasticsearch.search.SearchResults;
import play.mvc.*;
import controllers.Secure;
import models.*;
import play.libs.Crypto;


public class UpcomingBirthdays extends OBController {

  public static void showUpcomingBirthdays(Long id) {
    User current = User.findById(id);
    if (current == null)
      notFound();

    Date today = new Date();
    int day = today.getDate();
    Date thisWeek = new Date();
    thisWeek.setDate(day + 8);
    int thisYear = today.getYear()+1900;

    Date y = new Date();
    y.setDate(day);
    y.setYear(-10);


    List<User> todayBday = new ArrayList<User>();
    List<User> thisWeekBday = new ArrayList<User>();



    for(User u : current.friends) {
      Date friendBirthday = u.profile.birthday;

      if(friendBirthday == null) { //The user's friend did not set a birthday date field
        u.profile.birthday = y; //for testing purposes
        friendBirthday =  y; //for testing purposes
        //continue; //comment out for testing
      }
      friendBirthday.setYear(today.getYear());

      if (friendBirthday.equals(today))
        todayBday.add(u);
      else if(friendBirthday.before(thisWeek) && friendBirthday.after(today))
        thisWeekBday.add(u);


      //y.setYear(1992); //for testing purposes
    }

    render(thisWeekBday, todayBday, thisYear);
  }
}