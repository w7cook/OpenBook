package controllers;


import java.util.*;

import controllers.Secure;


import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Photos extends OBController {

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
      Application.notFound();
    }
    else {
      response.setContentTypeIfNotSet(photo.image.type());
      renderBinary(photo.image.get());
    }
  }

  public static void addPhoto(Photo photo) {
    User current = user();
    if (photo.image == null) {
      redirect("/users/" + current.id + "/photos");
    }
    photo.owner = current;
    photo.postedAt = new Date();
    photo.save();
    redirect("/users/" + current.id + "/photos");
  }

  public static void removePhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    photo.delete();
    redirect("/photos");
  }
}