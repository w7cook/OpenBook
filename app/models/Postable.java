package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
abstract class Postable extends Model {
 
  @OneToMany(mappedBy="postedObj", cascade=CascadeType.ALL)
  public List<Post> posts;
  
  public List<Post> getPosts() {
    return posts;
  }
  
  public Postable addPost(User author, String content) {
    new Post(this, author, content).save();
    return this;
  }
}
