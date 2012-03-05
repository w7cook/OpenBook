package controllers;
import java.util.*;
import play.*;
import play.mvc.*;
import models.*;

@With(Secure.class)
public class Photos extends OBController {

  /* All possible image mime types in a single regex. */
  public static final String IMAGE_TYPE = "^image/(gif|jpeg|pjpeg|png)$";
  public static final String TEST_IMAGE_TYPE = "^application/octet-stream; " +
                                               "charset=ISO-8859-1$";

  public static final int MAX_FILE_SIZE = 300 * 1024;  /* Max size in bytes. */

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
    if (photo.image == null ||
        photo.image.length() > MAX_FILE_SIZE ||
        !photo.image.type().matches(IMAGE_TYPE)) {
      redirect("/users/" + current.id + "/photos");
    }
    photo.owner = current;
    photo.postedAt = new Date();
    photo.save();
    redirect("/users/" + current.id + "/photos");
  }

  public static void removePhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo.owner.equals(user()))
      photo.delete();
    redirect("/photos");
  }
}
