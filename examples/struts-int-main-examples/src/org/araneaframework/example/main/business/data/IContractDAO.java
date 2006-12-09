package org.araneaframework.example.main.business.data;

public interface IContractDAO {
  void removeByPersonId(Long personId);
  void removeByCompanyId(Long companyId);
}