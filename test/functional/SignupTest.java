package functional;

import org.junit.*;

import controllers.Application;
import controllers.Signup;

import java.util.*;

import play.mvc.Http.Response;
import play.test.*;
import models.*;


public class SignupTest extends FunctionalTest {
  
  public static final String PASSWORD = "abcDEF1";
  public static final String EMAIL1 = "ab@cd.com";
  public static final String EMAIL2 = "ab@ef.com";
  public static final String USERNAME1 = "manuel";
  public static final String USERNAME2 = "manual";
  public static final String NAME = "george";
  
  @Before
  public void setup() {
    Fixtures.deleteDatabase();
    new User(EMAIL1, PASSWORD , USERNAME1 , "ell" , "kra").save();
    assertEquals(1, User.count());
  }
  
  @Test
  public void testGoodUser() {
    assertEquals(1, User.count());
    Map<String,String> parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    Response response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    assertEquals(1, TempUser.count());
  }
  
  @Test
  public void testBadUser() {
    
    Map<String,String> parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME1);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    Response response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", "ab");
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", "");
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    //signup_user(String firstName, String lastName, String username, String email, String email2, String password, String password2) {
  }

  @Test
  public void testBadEmail() {
    /* Non-matching emails */
    Map<String,String> parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL1);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    Response response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    /* Duplicate email */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL1);
    parameters.put("email2", EMAIL1);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    /* Invalid e-mail 1 */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", "abcd");
    parameters.put("email2", "abcd");
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());

    /* Invalid e-mail 2 */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", "abcd@f");
    parameters.put("email2", "abcd@f");
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());

    /* Invalid e-mail 3 */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", "abcd@.com");
    parameters.put("email2", "abcd@.com");
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
  }

  @Test
  public void testBadPassword() {
    /* Password too short */
    Map<String,String> parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", "ABCd1");
    parameters.put("password2", "ABCd1");
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    Response response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    /* No non-word character */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", "ABCdef");
    parameters.put("password2", "ABCdef");
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    /* No lower-case */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", "ABCDEF1");
    parameters.put("password2", "ABCDEF1");
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());

    /* No upper-case */
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", "abcdef1");
    parameters.put("password2", "abcdef1");
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
  }
  
  @Test
  public void testEmptyFields() {
    /* All empty */
    Map<String,String> parameters = new HashMap<String, String>();
    parameters.put("firstName", "");
    parameters.put("lastName", "");
    parameters.put("username", "");
    parameters.put("email", "");
    parameters.put("email2", "");
    parameters.put("password", "");
    parameters.put("password2", "");
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    Response response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    parameters = new HashMap<String, String>();
    parameters.put("firstName", "");
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", "");
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());

    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", "");
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());

    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", "");
    parameters.put("email2", EMAIL2);
    parameters.put("password", PASSWORD);
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
    
    parameters = new HashMap<String, String>();
    parameters.put("firstName", NAME);
    parameters.put("lastName", NAME);
    parameters.put("username", USERNAME2);
    parameters.put("email", EMAIL2);
    parameters.put("email2", EMAIL2);
    parameters.put("password", "");
    parameters.put("password2", PASSWORD);
    parameters.put("code", "d");
    parameters.put("randomID", "d");
    response = POST("/signup/signup_user", parameters);
    assertNotNull(response);
    assertEquals(1, User.count());
  }
  
}