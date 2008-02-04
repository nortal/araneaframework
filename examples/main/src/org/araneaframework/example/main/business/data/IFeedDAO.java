package org.araneaframework.example.main.business.data;

import java.util.List;
import org.araneaframework.example.main.business.model.FeedMO;
import org.araneaframework.example.main.business.model.UserMO;

public interface IFeedDAO {

  List getAll();

  void add(FeedMO object);

  void edit(FeedMO object);

  List findByUser(UserMO user);

  List findByUserId(Long userId);

}