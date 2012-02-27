package models;

import javax.persistence.*;
import play.db.jpa.*;


@Entity
public class Photo extends Model {
    
    public Blob image;

    @ManyToOne
	public User owner;
}