package functional;

import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

  @Test
  public void testLoginRedirect() {
    Response response = GET("/");
    assertStatus(Http.StatusCode.FOUND, response);
    assertHeaderEquals("Location", "/login", response);
  }
}
