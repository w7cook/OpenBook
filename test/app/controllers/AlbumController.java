/**AlbumController
 * purpose: controls all actions associated with an Alb
 *
 * created by: Chris. Cale
 *
 * email: collegeassignment@gmail.com
 *
 */

package controllers;

import java.util.*;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import controllers.Secure;
import models.*;


public class AlbumController extends OBController{

  /**create
   *
   * @param title of new album
   *
   * @return saves new album into database
   */
  public static void create(String title)//@Required(message = "A title is required")
  {
    Album newAlbum = new Album(title, Application.user());
    newAlbum.save();
  }

  /**delete
   *
   * @param Id of album
   *
   * @return none
   */
  public static void delete(Long Id){
    Album album = Album.findById(Id);
    album.delete();
    //redirect("/album");
  }

  /**getAlbum
   *
   * @param Id of album
   *
   * @return renders album to page
   */
  public static void getAlbum(Long Id){
    Album album = Album.findById(Id);
    if(album == null){
      notFound();
    }
    else{
      //      response.setContentTypeIfNotSet(album.image.type());
      //      renderBinary(album.image.get());
    }

  }


  /**addPhoto
   *
   * @param Long Id of album
   * @param String title
   * @return returns true if the name of album has changed
   */
  public static boolean changeTitle(Long Id, String newTitle){

    Album album = Album.findById(Id);
    String oldTitle = album.title;
    album.title = newTitle;
    //updateDate(album.albumId);
    return oldTitle != newTitle;
  }

  /**addPhotoToAlbum
   *
   * @param Long albumId
   * @param Long photoId
   * @return boolean if photo was added to album
   */
  public static boolean addPhotoToAlbum(Long albumId, Long photoId){

    Album album = Album.findById(albumId);
    Photo photo = Photo.findById(photoId);
    int oldSize = album.photos.size();
    album.photos.add(photo);
    int newSize = album.photos.size();
    //updateDate(album.albumId);
    return newSize != oldSize;
  }


  /**deletePhotoFromAlbum
   *
   * @param albumId
   * @param photoId
   * @return boolean true if photo is deleted from album
   */
  public static boolean deletePhotoFromAlbum(Long albumId, Long photoId){

    Album album = Album.findById(albumId);
    Photo photo = Photo.findById(photoId);
    int oldSize = album.photos.size();
    album.photos.remove(photo);
    int newSize = album.photos.size();
    //updateDate(album.albumId);
    return newSize != oldSize;
  }

  /**updateDate
   *
   * @param Id
   * @return boolean true if dateModified has changed
   */
  public static boolean updateDate(Long Id){
    Album album = Album.findById(Id);
    Date oldDate = album.lastDateModified;
    Date date = new Date();
    album.lastDateModified = date;
    return oldDate != album.lastDateModified;
  }

  /**getAll
   *
   * @return a list of all albums
   */
  public static List<Album> getAll(){
    return Album.findAll();// no idea how this works???
  }


  /**getNext
   *
   * @param albumId
   * @param index
   * @return photo at current index + 1
   */
  public static Photo getNext(Long albumId, int index){
    Album album = Album.findById(albumId);
    Photo nextPhoto = album.photos.get(index + 1);
    return nextPhoto;
  }

  /**getPrev
   *
   * @param albumId
   * @param index
   * @return photo at current index -1
   */
  public static Photo getPrev(Long albumId, int index){
    Album album = Album.findById(albumId);
    Photo nextPhoto = album.photos.get(index - 1);
    return nextPhoto;
  }


  //TODO: finish this method
  public static void export(){
    System.out.println("TO BE COMPLETED: EXPORT FUNCTIONALITY");
  }
}//end of class

