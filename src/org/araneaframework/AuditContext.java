package org.araneaframework;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface AuditContext extends Serializable {
  
  public void add(AuditRecord record);
  public List getAll();
  
  public interface AuditRecord extends Serializable {
    public Date getDateTime();
    public String toHTML();
  }
}
