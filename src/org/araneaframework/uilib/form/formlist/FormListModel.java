package org.araneaframework.uilib.form.formlist;

import java.io.Serializable;
import java.util.List;

public interface FormListModel extends Serializable {
  public List getRows() throws Exception;
}
