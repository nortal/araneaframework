package org.araneaframework.example.main.business.model;

import java.io.Serializable;

/**
 * @hibernate.class table="feeds" lazy="false"
 */
public class FeedMO implements Serializable {
  private Long id;
  private String url;
  private Long userId;
  
  /** @hibernate.id column="id" generator-class="increment" */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  /** @hibernate.property not-null="true" */ 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  
  /** @hibernate.property not-null="true" */
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
