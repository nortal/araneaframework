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

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.business.model.ContractMO;

/**
 * Quick'n'dirty fix for the ever annoying problem with broken contracts when persons or companies are deleted.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ContractDAO extends GeneralDAO implements IContractDAO {

  @SuppressWarnings("unchecked")
  public void removeByPersonId(Long personId) {
    List<ContractMO> l = (List<ContractMO>) getAll(ContractMO.class);
    List<ContractMO> toDelete = new ArrayList<ContractMO>();

    for (ContractMO contract : l) {
      if (contract.getPerson().getId().equals(personId)) {
        toDelete.add(contract);
      }
    }

    getHibernateTemplate().deleteAll(toDelete);
  }

  @SuppressWarnings("unchecked")
  public void removeByCompanyId(Long companyId) {
    List<ContractMO> l = (List<ContractMO>) getAll(ContractMO.class);
    List<ContractMO> toDelete = new ArrayList<ContractMO>();
    for (ContractMO contract : l) {
      if (contract.getCompany().getId().equals(companyId)) {
        toDelete.add(contract);
      }
    }
    getHibernateTemplate().deleteAll(toDelete);
  }
}
