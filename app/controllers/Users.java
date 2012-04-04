package controllers;

import org.elasticsearch.index.query.QueryBuilders;

import models.Status;
import models.User;
import play.modules.elasticsearch.ElasticSearch;
import play.modules.elasticsearch.search.SearchResults;
import play.mvc.Controller;


public class Users extends Controller {
  public static User searchForUser(String name) {
    SearchResults<User> list = ElasticSearch.search(
        QueryBuilders.fieldQuery("name", name), User.class);
    return list.objects.get(0);
  }

  public static void show(Long id){
    User person = User.findById(id);
    render(person);
  }      
}

