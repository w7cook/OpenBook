package controllers;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
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
public class PGEphotos extends Photos {
	
	//repeat for own flavor g/e
	//all photos n<0
	public static List<Photo> pagePhotos(Long id, int n) {
    User user =  user();
    List<Photo> photos;
    if(n >= 0){
    	photos = Photo.find("select p from Photo p where p.entityId = ? and p.photoType = ? order by id desc", id, Photo.type.PAGE).fetch(n);
    }
    else{
    	photos = Photo.find("select p from Photo p where p.entityId = ? and p.photoType = ? order by id desc", id, Photo.type.PAGE).fetch();
    }
    return photos;
  }
	
  //repeat for Groups/evenst (addGroupPhoto/addEventPhoto)
	public static void addPagePhoto(Long pid, File image) throws FileNotFoundException,
                                                 IOException {

    validation.keep(); /* Remember any errors after redirect. */
    
    if (image == null) {
      validation.addError("image", "You must specify an image to upload.");
      redirect("/users/" + user().id + "/photos");                            
    }

    shrinkImage(image);
    Photo photo = fileToPGEphoto(image, Photo.type.PAGE, pid);
    validation.match(photo.image.type(), IMAGE_TYPE);
    validation.max(photo.image.length(), MAX_FILE_SIZE);

    if (!validation.hasErrors()) {
      photo.save();
    }
    Page page = Page.findById(pid);
    User currentUser = user();
    Pages.display(page.id);
  }
  
  //get method from parent class Photos?
  private static void shrinkImage(File image) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(image);
    if (bufferedImage != null && (bufferedImage.getWidth() > MAX_PIXEL_SIZE ||
                                  bufferedImage.getHeight() > MAX_PIXEL_SIZE)) {
      Images.resize(image, image, MAX_PIXEL_SIZE, MAX_PIXEL_SIZE, true);
    }
  }
  
  public static Photo fileToPGEphoto(File image, Photo.type t, Long id) throws FileNotFoundException {
    Blob blob = new Blob();
    blob.set(new FileInputStream(image),
             MimeTypes.getContentType(image.getName()));
    
    if(t == Photo.type.PAGE){
    	Page p = Page.findById(id);
    	return new Photo(user(), blob, id, t, p);
    }
    //untested 
    if(t == Photo.type.GROUP){
    	Group g = Group.findById(id);
    	return new Photo(user(), blob, id, t, g);
    }
    if(t == Photo.type.EVENT){
    	Event e = Event.findById(id);
    	return new Photo(user(), blob, id, t, e);
    }
    //copy for group event
    //should never get here 
    else{return null;}
    
  }
  
  public static void setProfilePhoto(Long objId, Long photoId) {
    
	  Photo photo = Photo.findById(photoId);
	  Photo.type t = photo.photoType;
	  //repeat for g/e?
	  if(t == Photo.type.PAGE){
	  	Page p = Page.findById(objId);
	  	if (p.admin.equals(user())) {
  		  p.profilePhoto = photoId;
  		  p.save();
  	  }
  	  Pages.display(objId);
  	}
	}
  
}
