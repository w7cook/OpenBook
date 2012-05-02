package models;

import java.util.*;
import javax.persistence.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Status extends Commentable {

  private static final Pattern links_pattern = Pattern.compile("\\b?[@#]\\w*\\b");
  // private static final Pattern youtube_pattern = Pattern.compile();
  // private static final Pattern vimeo_pattern = Pattern.compile();


  @Lob
  public String content;

  @ManyToMany(cascade=CascadeType.PERSIST)
  public List<Tag> tags;

  @ManyToMany(cascade=CascadeType.PERSIST)
  public List<User> mentions;

  public Status(User author, String content) {
    super(author);
    this.comments = new ArrayList<Comment>();
    this.tags = new ArrayList<Tag>();
    this.mentions = new ArrayList<User>();
    this.content = content;
  }

  public String parseContent(String unlinked_content){
    Matcher links_matcher = links_pattern.matcher(unlinked_content);

    while(links_matcher.find() ){
      String match = links_matcher.group();
      if(match.startsWith("#")) { // tag
        String newTag = match.substring(1);
        tags.add(Tag.findOrCreateByName(newTag));
      }
      else if(match.startsWith("@")) { // mention
        User newMention = User.find("byUsername", match.substring(1)).first();
        mentions.add(newMention);
      }
      else
        System.out.print("Error occured");
    }
    return unlinked_content;
  }


  public static List<Status> findTaggedWith(String... tags) {
    return Status.find("select distinct p from Status p join p.tags as t where t.name in (:tags) group by p.id, p.owner, p.message, p.update_time having count(t.id) = :size"
                       ).bind("tags", tags).bind("size", tags.length).fetch();
  }
}
