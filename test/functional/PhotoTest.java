package functional;

import org.junit.*;
import java.util.*;
import java.io.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.vfs.*;
import play.db.jpa.*;
import models.*;
import controllers.*;

public class PhotoTest extends FunctionalTest {

  @Test
  public void test() {
		Fixtures.deleteDatabase();

		// Create a new user and save it
		User user = new User("bob@gmail.com", "secret", "Bob").save();
		assertEquals(1, User.findAll().size());

    Map<String, String> parameters = new HashMap<String,String>();
    parameters.put("authenticityToken", Scope.Session.current().getAuthenticityToken());
    parameters.put("username", "bob@gmail.com");
    parameters.put("password", "secret");
    parameters.put("remember", "true");
    Response response = POST("/login", parameters);
    assertNotNull(response);
    assertStatus(302, response);
    assertHeaderEquals("Location","/", response);

    parameters = new HashMap<String,String>();
    parameters.put("authenticityToken", Scope.Session.current().getAuthenticityToken());
    File file = VirtualFile.fromRelativePath("/test/test.gif").getRealFile();
    Map<String, File> files = new HashMap<String, File>();
    files.put("photo.image", file);

    response = POST("/photos", parameters, files);
    assertNotNull(response);
    assertStatus(302, response);
    assertHeaderEquals("Location", "/users/" + user.id + "/photos", response);

    Collection<Photo> photos = Photo.findAll();
    assertEquals(1, photos.size());
  }
}
