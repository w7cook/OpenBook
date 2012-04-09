package functional;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import org.junit.Before;

import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class EventFunctionalTest extends FunctionalTest {

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
  
  
  @Test
  public void testRedirect() {
    Response response = GET("/addEvent");
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location", "/addEvent", response);
    
    response = GET("/displayEvent");
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location", "/displayEvent", response);
    
    response = GET("/events");
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location", "/events", response);
  }
}
