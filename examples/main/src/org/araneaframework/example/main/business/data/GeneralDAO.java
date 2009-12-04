/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.example.main.business.data;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.araneaframework.example.main.business.model.GeneralMO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is general data access object. It can retrieve all objects by class, one object by Id and class, add or edit an
 * object or remove an object by it's Id and class.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class GeneralDAO implements IGeneralDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public <T extends GeneralMO> T getById(Class<T> clazz, Long id) {
    return this.entityManager.find(clazz, id);
  }

  @SuppressWarnings("unchecked")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public <T extends GeneralMO> List<T> getAll(Class<T> clazz) {
    return this.entityManager.createQuery("from " + clazz.getName()).getResultList();
  }

  @Transactional
  public <T extends GeneralMO> Long add(T object) {
    this.entityManager.persist(object);
    return object.getId();
  }

  @Transactional
  public <T extends GeneralMO> T edit(T object) {
    return this.entityManager.merge(object);
  }

  @Transactional
  public <T extends GeneralMO> void remove(Class<T> clazz, Long id) {
    this.entityManager.remove(getById(clazz, id));
  }
}
