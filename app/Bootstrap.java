import play.*;
import play.jobs.*;
import play.test.*;
import play.libs.Crypto;

import models.*;

import java.util.*;

@OnApplicationStart
public class Bootstrap extends Job {

  public void doJob() {
    // Check if the database is empty
    if(User.count() == 0) {
      Fixtures.loadModels("default-data.yml");//Default User
      Fixtures.loadModels("photo-data.yml");//initial photo data
      Fixtures.loadModels("skinTemplates.yml");//initial data for skin templates
      Fixtures.loadModels("initial-data.yml");//rest of the data
      List<User> users= User.findAll();
      for(User u : users) {
        u.password = Crypto.passwordHash(u.password);
        u.save();
      }
    }
  }
}