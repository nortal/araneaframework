package org.araneaframework.example.main.business.data;

import java.io.Serializable;

public interface IContractDAO extends Serializable {
  void removeByPersonId(Long personId);
  void removeByCompanyId(Long companyId);
}