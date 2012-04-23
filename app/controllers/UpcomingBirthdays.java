package controllers;

import java.util.*;

import org.elasticsearch.index.query.QueryBuilders;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    Date today = new Date();
    int day = today.getDate();
    Date thisWeek = new Date();
    thisWeek.setDate(day + 7);
    
    
    List<Relationship> friends = current.confirmedFriends();
    List<User> todayBday = new ArrayList<User>();
    List<User> thisWeekBday = new ArrayList<User>();

    for(Relationship x : friends)
    {
    	Date friendBirthday = x.to.getProfile().birthday;
   	
    	if(friendBirthday == null) //The user's friend did not set a birthday date field
    	{//for testing purposes
    		x.to.getProfile().birthday = today;
    		friendBirthday =  today;
//    		continue;
    	}
  	   friendBirthday.setYear(today.getYear());

    	if (friendBirthday.equals(today))
    	  todayBday.add(x.to);
    	else if(friendBirthday.before(thisWeek))
    	  thisWeekBday.add(x.to);
    }

    render(thisWeekBday, todayBday);
  }
}