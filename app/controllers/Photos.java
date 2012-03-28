package controllers;

import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
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
  public static final int MAX_PIXEL_SIZE = 1024;
  public static final int MAX_FILE_SIZE = 2 * 1024 * 1024;  /* Size in bytes. */

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
  public static Photo fileToPhoto(File image) throws FileNotFoundException {
    Blob blob = new Blob();
    blob.set(new FileInputStream(image),
             MimeTypes.getContentType(image.getName()));
    return new Photo(user(), blob);
  }
  
  /**
   * Convert a given File to a Photo model.Used in Bootstrap.java
   *
   * @param   image   the file to convert.
   * @return          the newly created Photo model.
   * @throws          FileNotFoundException
   */
  public static Photo initFileToPhoto(String path, String caption) throws FileNotFoundException {
    File image = new File(path);
    Blob blob = new Blob();
    blob.set(new FileInputStream(image),
             MimeTypes.getContentType(image.getName()));
    User user = User.find("username = ?", "default").first();//set owner as default owner
    Photo photo = new Photo(user, blob);
    photo.caption = caption;//give credit
    photo.save();
    return photo;
  }

  /**
   * Shrink the image to MAX_PIXEL_SIZE if necessary.
   *
   * @param   image   the file to convert.
   * @throws          IOException
   */
  private static void shrinkImage(File image) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(image);
    if (bufferedImage != null && (bufferedImage.getWidth() > MAX_PIXEL_SIZE ||
                                  bufferedImage.getHeight() > MAX_PIXEL_SIZE)) {
      Images.resize(image, image, MAX_PIXEL_SIZE, MAX_PIXEL_SIZE, true);
    }
  }

  public static void addPhoto(File image) throws FileNotFoundException,
                                                 IOException {

    validation.keep(); /* Remember any errors after redirect. */

    if (image == null) {
      validation.addError("image", "You must specify an image to upload.");
      redirect("/users/" + user().id + "/photos");
    }

    shrinkImage(image);
    Photo photo = fileToPhoto(image);
    validation.match(photo.image.type(), IMAGE_TYPE);
    validation.max(photo.image.length(), MAX_FILE_SIZE);

    if (!validation.hasErrors()) {
      photo.save();
    }
    redirect("/users/" + photo.owner.id + "/photos");
  }

  public static void removePhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo.owner.equals(user())) {
      photo.delete();
    }
    redirect("/users/" + photo.owner.id + "/photos");
  }
  
  public static void setProfilePhotoPage()
  {
    User user = user();
    //make sure get all of the photos
    List<Photo> photos;
    if (user.id == null) {
      photos = Photo.findAll();
    }
    else {
      photos = Photo.find("byOwner", user).fetch();
    }
    render(user,photos);
  }
  
  public static void setProfilePhoto(Long photoId) {
    
	  if(photoId != null){
	    User user = user();
	    Photo photo = Photo.findById(photoId);
  	  if (photo.owner.equals(user())) {
  		  user.profile.profilePhoto = photoId;
  		  user.profile.save();
  	  }
	  }
	  setProfilePhotoPage();//render page
  }
  
  /**
   * addProfilePhoto
   * 
   * just does the adding of the photo and then uses setProfilePhoto to set the profilePhoto
   * @param image
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void addProfilePhoto(File image) throws FileNotFoundException, IOException 
  {
    if(image != null){
      try{
        shrinkImage(image);
        Photo photo = fileToPhoto(image);
        validation.match(photo.image.type(), IMAGE_TYPE);
        validation.max(photo.image.length(), MAX_FILE_SIZE);
        
          if (validation.hasErrors()) {
            validation.keep(); /* Remember errors after redirect. */} 
          else {
            photo.save();
            User user = user();
            user.profile.profilePhoto = photo.id;
            user.profile.save();
          }
      }catch(FileNotFoundException f)
      {
        setProfilePhotoPage();//for if try to put in null file
      }
     }
      setProfilePhotoPage();//for if try to put in null file
   }
  
}
