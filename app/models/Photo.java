package models;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.persistence.*;
import play.libs.*;
import play.db.jpa.*;
import play.data.validation.*;
import net.coobird.thumbnailator.*;
import net.coobird.thumbnailator.geometry.*;
import net.coobird.thumbnailator.name.*;

@Entity
public class Photo extends Post {

  public static final int MAX_PIXEL_SIZE = 1024;

  @Required
  public Blob image;

  @Required
  public Blob thumbnail120x120,
              thumbnail50x50,
              thumbnail30x30;

  public Photo(User owner, File image) throws IOException,
                                              FileNotFoundException {
    this(owner, image, "");
  }

  public Photo(User owner, File image, String caption)
                                      throws IOException,
                                             FileNotFoundException {
    super(owner, owner, caption);
    shrinkImage(image);
    this.image = fileToBlob(image);
    this.createThumbnails();
  }

  public void updateImage(File image) throws IOException,
                                             FileNotFoundException {
    shrinkImage(image);
    this.image = fileToBlob(image);
    this.createThumbnails();
  }

  private void createThumbnails() throws IOException {
    this.thumbnail120x120 = this.createThumbnailBlob(120, 120);
    this.thumbnail50x50 = this.createThumbnailBlob(50, 50);
    this.thumbnail30x30 = this.createThumbnailBlob(30, 30);
  }

  private Blob createThumbnailBlob(int width, int height) throws IOException {
    File image = this.image.getFile();
    File thumbnail = Thumbnails.of(image)
                               .size(width, height)
                               .crop(Positions.CENTER)
                               .outputQuality(1.0)
                               .asFiles(Rename.NO_CHANGE)
                               .get(0);
    return fileToBlob(thumbnail);
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
      Thumbnails.of(image)
                .size(MAX_PIXEL_SIZE, MAX_PIXEL_SIZE)
                .outputQuality(1.0)
                .toFile(image);
    }
  }

  /**
   * Convert a given File to a Photo model.
   *
   * @param   image   the file to convert.
   * @return          the newly created Photo model.
   * @throws          FileNotFoundException
   */
  private static Blob fileToBlob(File image) throws FileNotFoundException {
    Blob blob = new Blob();
    blob.set(new FileInputStream(image),
             MimeTypes.getContentType(image.getName()));
    return blob;
  }
}

