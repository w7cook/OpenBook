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
    Date weekLater = new Date();
    weekLater.setDate(day + 7);

    Date almostWeekLater = new Date();
    almostWeekLater.setDate(day + 6);

    List<Relationship> friends = current.confirmedFriends();
    List<User> birthdayPeople = new ArrayList<User>();

    for(Relationship x : friends)
    {
    	Date friendBirthday = x.to.getProfile().birthday;

    	if(friendBirthday == null) //The user's friend did not set a birthday date field
    	{
    		continue;
    	}

    	friendBirthday.setYear(today.getYear());
    	if(friendBirthday.before(weekLater))
			birthdayPeople.add(x.to);
    }

    render(birthdayPeople, today, weekLater, almostWeekLater, day);

  }
}