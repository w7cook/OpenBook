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
import java.security.*;
import java.net.*;
import java.awt.image.*;

public class Photos extends OBController {

  /* All possible image mime types in a single regex. */
  public static final String IMAGE_TYPE = "^image/(gif|jpeg|pjpeg|png)$";
  public static final int MAX_FILE_SIZE = 2 * 1024 * 1024;  /* Size in bytes. */

  public static void photos(Long ownerId) {
    List<Photo> photos;
    if (ownerId == null) {
      photos = Photo.find("byOwner", user()).fetch();
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
      notFound("That photo does not exist.");
    }
    else {
      response.setContentTypeIfNotSet(photo.image.type());
      renderBinary(photo.image.get());
    }
  }

  /**
   * Convert a given File to a Photo model.Used in Bootstrap.java
   *
   * @param   image   the file to convert.
   * @return          the newly created Photo model.
   * @throws          FileNotFoundException
   */
  public static Photo initFileToPhoto(String path, String caption)
                                                  throws IOException,
                                                         FileNotFoundException {
    File image = new File(path);
    User user = User.find("username = ?", "default").first();//set owner as default owner
    Photo photo = new Photo(user, image, caption);
    photo.save();
    return photo;
  }

  public static void addPhoto(File image) throws FileNotFoundException,
                                                 IOException {
    validation.keep(); /* Remember any errors after redirect. */

    if (image == null ||
        !MimeTypes.getContentType(image.getName()).matches(IMAGE_TYPE)) {
      validation.addError("image",
                          "validation.image.type");
      redirect("/users/" + user().id + "/photos");
    }

    Photo photo = new Photo(user(), image);
    validation.max(photo.image.length(), MAX_FILE_SIZE);

    if (!validation.hasErrors()) {
      photo.save();
      
      //Add a TimelineEvent to the Timeline, if the user has one
      if (photo.owner.timeline != null)
	  photo.owner.timeline.addEvent(photo.owner.id, photo, TimelineModel.Action.CREATE, new Vector<User>(), photo.owner.first_name + " " + photo.owner.last_name + " uploaded a picture ");    
    }

    redirect("/users/" + photo.owner.id + "/photos");
  }

  public static void removePhoto(Long photoId) {
    Photo photo = Photo.findById(photoId);
    if (photo == null)
      notFound("That photo does not exist.");
    if (!photo.owner.equals(user()))
      forbidden();
    photo.delete();
    redirect("/users/" + photo.owner.id + "/photos");
  }


  public static void setProfilePhotoPage()  {
    User user = user();
    List<Photo> photos = Photo.find("byOwner", user).fetch();
    render(user,photos);
  }


  public static void changeBGImage() {
    User user = user();
    photos(user.id);
  }

  public static void setProfilePhoto(Long photoId) {
    User user = user();
    Photo photo = Photo.findById(photoId);
    if (photo == null)
      notFound("That photo does not exist.");

    if (!photo.owner.equals(user()))
      forbidden();

    user.profile.profilePhoto = photo;
    user.profile.save();
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
  public static void addProfilePhoto(File image) throws FileNotFoundException, IOException {
    if(image != null) {
      try {
        Photo photo = new Photo(user(), image);
        validation.match(photo.image.type(), IMAGE_TYPE);
        validation.max(photo.image.length(), MAX_FILE_SIZE);

        if (validation.hasErrors()) {
          validation.keep(); /* Remember errors after redirect. */}
        else {
          photo.save();
          User user = user();
          user.profile.profilePhoto = photo;
          user.profile.save();
        }
      } catch(FileNotFoundException f) {
        setProfilePhotoPage();//for if try to put in null file
      }
    }
    setProfilePhotoPage();//for if try to put in null file
  }

  /**
   * set gravatar to the profile photo
   */

  public static void setGravatar(String gravatarEmail) throws FileNotFoundException, IOException {
    //first takes the user's email and makes it into the correct hex string
    User u = user();
    String hash = md5Hex((gravatarEmail.trim()).toLowerCase());
    String urlPath = "http://www.gravatar.com/avatar/"+hash+".jpg"+
      "?" +//parameters
      "size=120&d=mm";
    URL url = new URL(urlPath);
    BufferedImage image = ImageIO.read(url);
    if(u.profile.gravatarPhoto == null) { // don't yet have a gravatarPhoto
      try {
        File gravatar = new File(hash+".jpg");
        ImageIO.write(image, "jpg",gravatar);

        if(gravatar != null) {
          Photo photo = new Photo(user(), gravatar);
          validation.match(photo.image.type(), IMAGE_TYPE);
          validation.max(photo.image.length(), MAX_FILE_SIZE);

          if (validation.hasErrors()) {
            validation.keep(); /* Remember errors after redirect. */}
          else {
            photo.save();
            User user = user();
            user.profile.profilePhoto = photo;

            //set gravatarPhoto id
            u.profile.gravatarPhoto = photo;
            user.profile.save();
          }
          gravatar.delete();
        }
      } catch(Exception f) {
        redirect("https://en.gravatar.com/site/signup/");
      }
    }
    else { // have already added the gravatar picture, so we need to displace pic.
      Photo oldPhoto = Photo.findById(u.profile.gravatarPhoto.id);
      try{
        File gravatar = new File(hash+".jpg");
        ImageIO.write(image, "jpg",gravatar);

        if(gravatar != null){
          oldPhoto.updateImage(gravatar);
          validation.match(oldPhoto.image.type(), IMAGE_TYPE);
          validation.max(oldPhoto.image.length(), MAX_FILE_SIZE);

          if (validation.hasErrors()) {
            validation.keep(); /* Remember errors after redirect. */}
          else {
            oldPhoto.save();
            User user = user();
            user.profile.profilePhoto = oldPhoto;

            //set gravatarPhoto id
            u.profile.gravatarPhoto = oldPhoto;
            user.profile.save();
          }

        }

        gravatar.delete();//delete file. We don't need it
      }
      catch(Exception f) {
        redirect("https://en.gravatar.com/site/signup/");
      }
    }

    //if reach here have successfully changed the gravatar so we reset the email
    u.profile.gravatarEmail = gravatarEmail;
    u.profile.save();

    setProfilePhotoPage();//render page
  }

  /**
   * helper method for gravatar
   * makes String into md5hex
   * @param message
   * @return
   */
  private static String md5Hex (String message) {
    try {
      MessageDigest md =
        MessageDigest.getInstance("MD5");
      return Codec.byteToHexString(md.digest(message.getBytes("CP1252")));
    } catch (NoSuchAlgorithmException e) {
    } catch (UnsupportedEncodingException e) {
    }
    return null;
  }
}

