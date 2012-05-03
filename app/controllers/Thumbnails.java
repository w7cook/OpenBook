package controllers;

import play.i18n.Messages;
import play.db.jpa.Blob;
import models.Photo;

public class Thumbnails extends OBController {

  private static Photo getPhotoIfVisible(long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo == null || !photo.visible(user())) {
      notFound(Messages.get("photo.notFound"));
    }
    return photo;
  }

  private static void renderBlob(Blob thumbnail) {
    response.setContentTypeIfNotSet(thumbnail.type());
    renderBinary(thumbnail.get());
  }

  public static void get120x120(Long photoId) {
    Photo photo = getPhotoIfVisible(photoId);
    renderBlob(photo.thumbnail120x120);
  }

  public static void get50x50(Long photoId) {
    Photo photo = getPhotoIfVisible(photoId);
    renderBlob(photo.thumbnail50x50);
  }

  public static void get30x30(Long photoId) {
    Photo photo = getPhotoIfVisible(photoId);
    renderBlob(photo.thumbnail30x30);
  }
}
