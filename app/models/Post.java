package models;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.persistence.*;

import controllers.Security;

import play.db.jpa.*;
import play.modules.elasticsearch.annotations.ElasticSearchable;


@Entity
public class Post extends Commentable {
  
  private static final Pattern links_pattern = Pattern.compile("\\b?[@#]\\w*\\b");
  
  @ManyToOne
  public Postable postedObj; // The postable object this post was posted on.

  @ManyToOne
  public User author; // The User who authored the status update
  
  @ManyToMany(cascade=CascadeType.PERSIST)
  public Set<Tag> tags;
  
  @ManyToMany(cascade=CascadeType.PERSIST)
  public List<User> mentions;

  @Lob
  public String content;

  private static final int TEASER_LENGTH = 150;

  public Post(Postable postedObj, User author, String content) {
    this.postedObj = postedObj;
    this.tags = new TreeSet<Tag>();
    this.mentions = new ArrayList<User>();
    this.author = author;
    this.content = parseContent(content);
  }
  
  public String contentTeaser() {
	  if (this.content.length() < TEASER_LENGTH) {
		  return this.content;
	  } else {
		  return this.content.substring(0, TEASER_LENGTH);
	  }
  }

  public Post previous() {
    return Post.find("author = ? AND date < ? order by date desc",
                     this.author, this.createdAt).first();
  }

  public Post next() {
    return Post.find("date > ? order by date asc", this.createdAt)
      .first();
  }

  public boolean byCurrentUser() {
    return author.email.equals( Security.connected() );
  }
  public List<Object> getOlderComments(int n){
	  ArrayList<Object> list = (ArrayList<Object>) Comment.find("FROM Comment c WHERE c.parentObj.id = ? order by c.updatedAt desc", this.id).fetch();
	  if(n<list.size()){
		  while(n>0){list.remove(0);n--;}
	  }
	  return list;
  }
  public List<Object> getSomeComments(int n){
	  ArrayList<Object> ret = new ArrayList<Object>();
	  ArrayList<Object> list = (ArrayList<Object>) Comment.find("FROM Comment c WHERE c.parentObj.id = ? order by c.updatedAt desc", this.id).fetch(n);

	  for(int i=list.size()-1;i>=0;i--)
		  ret.add(list.get(i));

	  return ret;
  }
  public ArrayList<Object> getComments(){
	  return (ArrayList<Object>) Comment.find("FROM Comment c WHERE c.parentObj.id = ? order by c.updatedAt asc", this.id).fetch();
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
          unlinked_content = unlinked_content.replace(match, ("<a href=\"#\">" + newMention.name + "</a>"));
          mentions.add(newMention);
        }
      }
      else
       System.out.print("Error occured");
    }
    
    return unlinked_content;
	}
	
  
  public static List<Status> findTaggedWith(String... tags) {
    return Status.find(
            "select distiinct p from Status p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.message, p.update_time having count(t.id) = :size"
    ).bind("tags", tags).bind("size", tags.length).fetch();
  }
}
