package org.araneaframework.example.main.business.data;

import java.util.List;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class UserDAO extends GeneralDAO {
  public List findByName(String name) {
    return getHibernateTemplate().find("from UserMO u where u.name=?", name);
  }
}
