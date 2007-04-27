package org.araneaframework.uilib.form.formlist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.araneaframework.uilib.form.formlist.FormListModel;

public class MapFormListModel implements FormListModel {
  private Map map;
  
  public MapFormListModel(Map map) {
    this.map = map;
  }
  
  public List getRows() {
    return new ArrayList(map.values());
  }
  
  public void refresh() throws Exception {
  }
}
