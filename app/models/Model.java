package models;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import play.modules.elasticsearch.annotations.ElasticSearchIgnore;

@MappedSuperclass
public class Model extends play.db.jpa.Model {

  @ElasticSearchIgnore      
  public Date createdAt;
  @ElasticSearchIgnore
  public Date updatedAt;
  
  @PrePersist
  void onPrePersist() {
    if (this.createdAt == null) {
      this.createdAt = new Date();
    }
    this.updatedAt = this.createdAt;
  }

  @PreUpdate
  void onPreUpdate() {
    this.updatedAt = new Date();
  }
}
