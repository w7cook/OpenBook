package functional;

import org.junit.*;
import org.junit.Before;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.vfs.*;
import play.db.jpa.*;
import models.*;
import controllers.*;

public class PhotoTest extends FunctionalTest {

  /* Test constants. */
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String EMAIL = "email@email.com";

  public static final String FILENAME_GOOD_GIF = "/test/data/plant.gif";
  public static final String FILENAME_GOOD_JPG = "/test/data/plant.jpg";
  public static final String FILENAME_BAD_SIZE = "/test/data/plant.png";
  public static final String FILENAME_BAD_TYPE = "/test/data/plant.tif";
  public static final String FILENAME_LARGE = "/test/data/plant_2560x1600.jpg";

  public User user;

  /**
   * Setup a new database with a single user, then perform an actual login.
   */
  @Before
  public void setup() {
    Fixtures.deleteDatabase();

    /* Create a new user and save it. */
    this.user = new User(EMAIL, PASSWORD, USERNAME).save();
    assertEquals(1, User.findAll().size());

    /* Log in the user. */
    Map<String,String> parameters = this.setupParameters();
    parameters.put("username", EMAIL);
    parameters.put("password", PASSWORD);
    Response response = POST("/login", parameters);
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location", "/", response);
  }

  /**
   * Initialize the key-value parameters for a POST command with the auth token
   * from the current session.
   *
   * @return A map containing a single entry, the authenticity token.
   */
  private Map<String,String> setupParameters() {
    Map<String, String> parameters = new HashMap<String,String>();
    parameters.put("authenticityToken",
                   Scope.Session.current().getAuthenticityToken());
    return parameters;
  }

  /**
   * Initialize the key-value parameters for a POST command with the given file
   * as the only parameter.
   *
   * @return A map containing a single entry, the file for upload.
   */
  private Map<String,File> setupUpload(String filename) {
    File image = VirtualFile.fromRelativePath(filename).getRealFile();
    Map<String,File> files = new HashMap<String,File>();
    files.put("image", image);
    return files;
  }

  @Test
  public void testGoodUploads() {
    Response response = POST("/photos",
                             this.setupParameters(),
                             this.setupUpload(FILENAME_GOOD_GIF));
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location",
                       "/users/" + this.user.id + "/photos",
                       response);
    assertEquals(1, Photo.count());

    response = POST("/photos",
                    this.setupParameters(),
                    this.setupUpload(FILENAME_GOOD_JPG));
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location",
                       "/users/" + this.user.id + "/photos",
                       response);
    assertEquals(2, Photo.count());
  }

  @Test
  public void testNullUpload() {
    Map<String,File> files = new HashMap<String,File>();
    files.put("image", null);
    Response response = POST("/photos",
                             this.setupParameters(),
                             files);
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location",
                       "/users/" + this.user.id + "/photos",
                       response);
    assertEquals(0, Photo.count());
  }

  @Test
  public void testBadUploads() {
    Response response = POST("/photos",
                             this.setupParameters(),
                             this.setupUpload(FILENAME_BAD_SIZE));
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location",
                       "/users/" + this.user.id + "/photos",
                       response);
    assertEquals(0, Photo.count());

    response = POST("/photos",
                    this.setupParameters(),
                    this.setupUpload(FILENAME_BAD_TYPE));
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location",
                       "/users/" + this.user.id + "/photos",
                       response);
    assertEquals(0, Photo.count());
  }

  @Test
  public void testResize() throws IOException {
    File image = VirtualFile.fromRelativePath(FILENAME_LARGE).getRealFile();
    BufferedImage bufferedImage = ImageIO.read(image);
    assertEquals(2560, bufferedImage.getWidth());
    assertEquals(1600, bufferedImage.getHeight());

    Response response = POST("/photos",
                             this.setupParameters(),
                             this.setupUpload(FILENAME_LARGE));
    assertNotNull(response);
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location",
                       "/users/" + this.user.id + "/photos",
                       response);
    List<Photo> photos = Photo.findAll();
    assertEquals(1, photos.size());

    bufferedImage = ImageIO.read(photos.get(0).image.getFile());
    assertEquals(1024, bufferedImage.getWidth());
    assertEquals(640, bufferedImage.getHeight());
  }
}
