/**
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
**/

package org.araneaframework.example.main.business.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.example.main.business.model.ContractMO;

/**
 * Quick'n'dirty fix for the ever annoying problem with broken contracts when persons or companies are deleted.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ContractDAO extends GeneralDAO {

  public void removeByPersonId(Long personId) {
    List l = getAll(ContractMO.class);
    List toDelete = new ArrayList();
    for (Iterator i = l.iterator(); i.hasNext(); ) {
      ContractMO contract = (ContractMO) i.next();
      if (contract.getPerson().getId().equals(personId)) {
        toDelete.add(contract);
      }
    }
    getHibernateTemplate().deleteAll(toDelete);
  }

  public void removeByCompanyId(Long companyId) {
    List l = getAll(ContractMO.class);
    List toDelete = new ArrayList();
    for (Iterator i = l.iterator(); i.hasNext(); ) {
      ContractMO contract = (ContractMO) i.next();
      if (contract.getCompany().getId().equals(companyId)) {
        toDelete.add(contract);
      }
    }
    getHibernateTemplate().deleteAll(toDelete);
  }
}
