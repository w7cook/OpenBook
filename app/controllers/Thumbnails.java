package controllers;

import models.Photo;

public class Thumbnails extends OBController {
  public static void get120x120(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo == null) {
      notFound("That photo does not exist.");
    }
    else {
      response.setContentTypeIfNotSet(photo.thumbnail120x120.type());
      renderBinary(photo.thumbnail120x120.get());
    }
  }

  public static void get50x50(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo == null) {
      notFound("That photo does not exist.");
    }
    else {
      response.setContentTypeIfNotSet(photo.thumbnail50x50.type());
      renderBinary(photo.thumbnail50x50.get());
    }
  }

  public static void get30x30(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo == null) {
      notFound("That photo does not exist.");
    }
    else {
      response.setContentTypeIfNotSet(photo.thumbnail30x30.type());
      renderBinary(photo.thumbnail30x30.get());
    }
  }
}
