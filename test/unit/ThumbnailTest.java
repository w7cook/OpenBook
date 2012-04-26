package unit;

import org.junit.*;
import play.test.*;
import models.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import play.vfs.VirtualFile;

public class ThumbnailTest extends UnitTest {

  public static final String FILENAME= "/test/data/plant.jpg";

  User user;

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
		user = new User("bob@gmail.com", "secret", "Bob").save();
	}

  @Test
  public void testThumbnails() throws IOException {
    File image = VirtualFile.fromRelativePath(FILENAME).getRealFile();
    BufferedImage bufferedImage = ImageIO.read(image);
    assertEquals(640, bufferedImage.getWidth());
    assertEquals(480, bufferedImage.getHeight());

    Photo photo = new Photo(user, image);
    bufferedImage = ImageIO.read(photo.image.getFile());
    assertEquals(640, bufferedImage.getWidth());
    assertEquals(480, bufferedImage.getHeight());

    bufferedImage = ImageIO.read(photo.thumbnail120x120.getFile());
    assertEquals(120, bufferedImage.getWidth());
    assertEquals(120, bufferedImage.getHeight());

    bufferedImage = ImageIO.read(photo.thumbnail50x50.getFile());
    assertEquals(50, bufferedImage.getWidth());
    assertEquals(50, bufferedImage.getHeight());

    bufferedImage = ImageIO.read(photo.thumbnail30x30.getFile());
    assertEquals(30, bufferedImage.getWidth());
    assertEquals(30, bufferedImage.getHeight());
  }
}  
