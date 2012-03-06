package controllers;

import java.util.*;
import java.io.*;
import play.*;
import play.data.validation.Error;
import play.libs.*;
import play.mvc.*;
import play.db.jpa.*;
import models.*;

@With(Secure.class)
public class Photos extends OBController {

  /* All possible image mime types in a single regex. */
  public static final String IMAGE_TYPE = "^image/(gif|jpeg|pjpeg|png)$";
  public static final int MAX_FILE_SIZE = 500 * 1024;  /* Max size in bytes. */

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

  /**
   * Convert a given File to a Photo model.
   *
   * @param   image   the file to convert.
   * @return          the newly created Photo model.
   * @throws          FileNotFoundException
   */
  private static Photo fileToPhoto(File image) throws FileNotFoundException {
    Blob blob = new Blob();
    blob.set(new FileInputStream(image),
             MimeTypes.getContentType(image.getName()));
    return new Photo(user(), blob);
  }

  public static void addPhoto(File image) throws FileNotFoundException {
    Photo photo = fileToPhoto(image);

    validation.match(photo.image.type(), IMAGE_TYPE);
    validation.max(photo.image.length(), MAX_FILE_SIZE);

    if (validation.hasErrors()) {
      validation.keep(); /* Remember errors after redirect. */
    } else {
      photo.save();
    }
    redirect("/users/" + photo.owner.id + "/photos");
  }

  public static void removePhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo.owner.equals(user())) {
      photo.delete();
    }
    redirect("/photos");
  }
}
