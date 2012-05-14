package models;

import javax.persistence.Entity;

import play.utils.HTML;

@Entity
public class Checkin extends Post {

  public Checkin(Postable postedObj, User author, String location, String name,
      String address) {
    super(postedObj, author, "");
    StringBuilder sb = new StringBuilder(HTML.htmlEscape(play.i18n.Messages
        .get("checkin.checkedinat") +name + "\n" + address + "\n"));
    sb.append("<img src='http://maps.googleapis.com/maps/api/staticmap?center=")
        .append(location)
        .append("&zoom=15&size=320x150&sensor=false&markers=color:orange%7C")
        .append(location).append("'/>");
    this.content = sb.toString();
  }

}
