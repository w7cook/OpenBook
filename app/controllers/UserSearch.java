package controllers;

import java.util.List;

import org.hibernate.Hibernate;

import play.db.Model;
import play.db.jpa.Transactional;
import play.exceptions.TemplateNotFoundException;
import play.modules.elasticsearch.search.SearchResults;
import play.mvc.Before;
import controllers.*;
import controllers.elasticsearch.ElasticSearchController;
import controllers.elasticsearch.ElasticSearchController.ObjectType;
import models.*;

@ElasticSearchController.For(User.class)
public class UserSearch extends OBController {

  /**
   * Index.
   */
  public static void index() {
    if (getControllerClass() == ElasticSearchController.class) {
      forbidden();
    }
    render("UserSearch/index.html");
  }

  /**
   * Search.
   *
   * @param page the page
   * @param search the search
   * @param searchFields the search fields
   * @param orderBy the order by
   * @param order the order
   */
  public static void search(int page, String search, String searchFields, String orderBy, String order) {
    ObjectType type = ObjectType.get(getControllerClass());
    notFoundIfNull(type);
    if (page < 1) {
      page = 1;
    }
    searchFields = "email, first_name, last_name, name";

    SearchResults<Model> results = type.findPage(page, search, searchFields, orderBy, order, (String) request.args.get("where"));
    List<Model> objects = results.objects;
    Long count = results.totalCount;
    Long totalCount = type.count(null, null, (String) request.args.get("where"));
    try {
      render(type, objects, count, totalCount, page, orderBy, order);
    } catch (TemplateNotFoundException e) {
      render("UserSearch/search.html", type, objects, count, totalCount, page, orderBy, order);
    }
  }

}

