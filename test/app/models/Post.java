package models;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import play.utils.HTML;

import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;
import play.modules.elasticsearch.annotations.ElasticSearchable;


@Entity
public class Post extends Commentable {

  private static final Pattern links_pattern = Pattern.compile("\\b?[@#]\\w+\\b");

  @ManyToOne
  public Postable postedObj; // The postable object this post was posted on.

  @ManyToMany(cascade=CascadeType.PERSIST)
  public Set<Tag> tags;

  @ManyToMany(cascade=CascadeType.PERSIST)
  public List<User> mentions;

  @Lob
  public String content;

  private static final int TEASER_LENGTH = 150;

  public Post(Postable postedObj, User author, String content) {
    super(author);
    this.postedObj = postedObj;
    this.tags = new TreeSet<Tag>();
    this.mentions = new ArrayList<User>();
    this.content = parseContent(HTML.htmlEscape(content));
  }

  public String contentTeaser() {
    return this.content.length() < TEASER_LENGTH ? this.content : this.content.substring(0, TEASER_LENGTH);
  }

  public Post previous() {
    return Post.find("owner = ? AND date < ? order by date desc",
                     this.owner, this.createdAt).first();
  }

  public Post next() {
    return Post.find("date > ? order by date asc", this.createdAt)
      .first();
  }

  public String parseContent(String unlinked_content){
    Matcher links_matcher = links_pattern.matcher(unlinked_content);

    while(links_matcher.find() ){
      String match = links_matcher.group();
      if(match.startsWith("#")) { // tag
        String tag = match.substring(1);
        Tag newTag = Tag.findOrCreateByName(tag);
        tags.add(newTag);
        unlinked_content = unlinked_content.replace(match, ("<a href=\"#\"}>" + match + "</a>"));
      }
      else if(match.startsWith("@")) { // mention
        User newMention = User.find("byUsername", match.substring(1)).first();
        if(newMention != null){
          unlinked_content = unlinked_content.replace(match, ("<a href=\"/users/" + newMention.id + "\">" + newMention.name + "</a>"));
          mentions.add(newMention);
        }
      }
      else
        System.out.print("Error occured"); // not good
    }
    return unlinked_content;
  }


  // What is this used for?
  public static List<Status> findTaggedWith(String... tags) {
    return Status.find("select distinct p from Status p join p.tags as t where t.name in (:tags) group by p.id, p.owner, p.message, p.update_time having count(t.id) = :size"
                       ).bind("tags", tags).bind("size", tags.length).fetch();
  }

  public static List<Post> findVisible(User user) {
    return Post.find("SELECT DISTINCT p FROM Post AS p LEFT JOIN p.whitelist AS whitelist LEFT JOIN p.owner.friends AS friends WHERE (p.owner = ?1) OR (p.visibility = ?2) OR (?1 IN whitelist) OR (p.visibility = ?3 AND ?1 IN friends) ORDER BY p.updatedAt DESC", user, Visibility.PUBLIC, Visibility.FRIENDS).fetch();
  }
}