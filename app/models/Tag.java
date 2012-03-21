package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Tag extends Model implements Comparable<Tag> {
 
    @Required
    public String name;
 
    private Tag(String name) {
        this.name = name;
    }
 
    public String toString() {
        return name;
    }
 
    public int compareTo(Tag otherTag) {
        return name.compareTo(otherTag.name);
    }
    
    public static Tag findOrCreateByName(String name) {
        Tag tag = Tag.find("byName", name).first();
        if(tag == null) {
            tag = new Tag(name);
        }
        return tag;
    }
}
