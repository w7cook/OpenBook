package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;
import models.*;

import java.util.*;

import javax.persistence.*;

public class FThreads extends OBController {
  public static void listSingle(Long threadId) {
    FThread thread = FThread.findById(threadId);
    render(thread);
  }

  @OneToMany(mappedBy = "fthread", cascade = CascadeType.ALL)
  public List<Comment> allComments;
}
