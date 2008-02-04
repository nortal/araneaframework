package org.araneaframework.example.main.business.model;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @hibernate.class table="users" lazy="false"
 */
public class UserMO implements GeneralMO {
  private Long id;
  private String name;
  private String password;
  
  /**
   * @hibernate.id column="id" generator-class="increment"
   */
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  
  /**
   * @hibernate.property not-null="true"
   */
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @hibernate.property not-null="true"
   */
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
}
