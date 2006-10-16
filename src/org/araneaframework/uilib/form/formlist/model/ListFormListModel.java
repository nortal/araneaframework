package org.araneaframework.uilib.form.formlist.model;

import java.util.List;
import org.araneaframework.uilib.form.formlist.FormListModel;

public class ListFormListModel implements FormListModel {
  private List list;
  
  public ListFormListModel(List list) {
    this.list = list;
  }

  public List getRows() {
    return list;
  }

}
