package controllers;


import java.util.*;

import controllers.Secure;


import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Photos extends Controller {

  @Before
  static void setConnectedUser() {
    if (Security.isConnected()) {
      renderArgs.put("currentUser", user());
    }
  }

  public static User user() {
    assert Secure.Security.connected() != null;
    return User.find("byEmail", Secure.Security.connected()).first();
  }

  public static void photos(Long ownerId) {
    List<Photo> photos;
    if (ownerId == null) {
      photos = Photo.findAll();
    }
    else {
      User user = User.findById(ownerId);
      photos = Photo.find("byOwner", user).fetch();
    }
    render(photos);
  }

  public static void getPhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo == null) {
      response.status = Http.StatusCode.NOT_FOUND;
      renderText("");
    }
    else {
      response.setContentTypeIfNotSet(photo.image.type());
      renderBinary(photo.image.get());
    }
  }

  public static void addPhoto(Photo photo) {
    if (photo.image == null);{
      redirect("/photos");
    }
    photo.owner = user();
    photo.postedAt = new Date();
    photo.save();
    redirect("/photos");
  }

  public static void removePhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    photo.delete();
    redirect("/photos");
  }
}