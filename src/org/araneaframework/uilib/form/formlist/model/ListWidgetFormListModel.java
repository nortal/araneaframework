package org.araneaframework.uilib.form.formlist.model;

import java.util.List;
import org.araneaframework.uilib.form.formlist.FormListModel;
import org.araneaframework.uilib.list.ListWidget;

public class ListWidgetFormListModel implements FormListModel {
  private ListWidget listWidget;

  public ListWidgetFormListModel(ListWidget listWidget) {
    this.listWidget = listWidget;
  }

  public List getRows() {
    listWidget.refresh();
    return listWidget.getItemRange();
  }

}
