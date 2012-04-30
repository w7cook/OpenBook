package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;
import models.*;

import java.util.*;

import javax.persistence.*;

public class Categories extends OBController {
  final static int MAX_CATEGORIES = 8;
	
	
  public static void listAll() {
    List<Category> Categories = Category.find("order by name").fetch();
    long nextId = 1;
    
    Iterator<Category> iterator = Categories.iterator();
    
    while (iterator.hasNext())
    {
    	Category cat = iterator.next();
    	nextId = cat.id;
    }
    
    
    render(Categories, nextId);
  }

  public static void listSingle(Long catId) {
	User _user = Application.user();
    Category category = Category.findById(catId);
    List<FThread> FThreads = FThread.find("category = ? order by postedAt desc", category).fetch(10);
    render(FThreads, category, _user);
  }
  
  public static void newCategory (String name, String description) {
	if (Category.findAll().size() < MAX_CATEGORIES)
	{
		final Category category = new Category(name, description).save();
	}
  }
  
  public static void deleteCategory (Long categoryId) {
	Category c = Category.findById(categoryId);
	c.delete();
  }
}
