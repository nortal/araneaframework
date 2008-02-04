package org.araneaframework.example.main.business.data;

import java.util.List;
import org.araneaframework.example.main.business.model.FeedMO;
import org.araneaframework.example.main.business.model.UserMO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class FeedDAO extends HibernateDaoSupport implements IFeedDAO {
  /* (non-Javadoc)
   * @see org.araneaframework.example.main.business.data.IFeedDAO#getAll(java.lang.Class)
   */
  public List getAll() {
    return getHibernateTemplate().find("from " + FeedMO.class.getName());
  }

  /* (non-Javadoc)
   * @see org.araneaframework.example.main.business.data.IFeedDAO#add(org.araneaframework.example.main.business.model.FeedMO)
   */
  public void add(FeedMO object) {
    getHibernateTemplate().save(object);
  }
    
  /* (non-Javadoc)
   * @see org.araneaframework.example.main.business.data.IFeedDAO#edit(org.araneaframework.example.main.business.model.FeedMO)
   */
  public void edit(FeedMO object) {
    getHibernateTemplate().update(object);
  }
  
  /* (non-Javadoc)
   * @see org.araneaframework.example.main.business.data.IFeedDAO#findByUser(org.araneaframework.example.main.business.model.UserMO)
   */
  public List findByUser(UserMO user) {
    return findByUserId(user.getId());
  }
  
  public List findByUserName(String userName) {
    return getHibernateTemplate().find("from FeedMO feed where feed.userId=(select user.id from UserMO user where user.name = ?)",userName);
  }
  
  /* (non-Javadoc)
   * @see org.araneaframework.example.main.business.data.IFeedDAO#findByUserId(java.lang.Long)
   */
  public List findByUserId(Long userId) {
    return getHibernateTemplate().find("from FeedMO where USERID=?", userId);
  }
}
