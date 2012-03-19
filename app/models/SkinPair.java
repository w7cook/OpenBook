package models;

import java.lang.reflect.Field;
import java.util.*;
import javax.persistence.*;

import controllers.Security;
import play.db.jpa.*;


/**
 * SkinPair
 * @author Lauren Yew
 * 
 * these are the attributes for a Skin
 */
@Entity
public class SkinPair extends Model {
 
  @ManyToOne
  public Skin attachedSkin;
  
  public String name;
  public String value;
  
  public SkinPair(Skin s, String n, String v)
  {
    this.attachedSkin = s;
    this.name = n;
    this.value = v;
    
  }
}