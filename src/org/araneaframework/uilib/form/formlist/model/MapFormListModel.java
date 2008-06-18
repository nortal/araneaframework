package org.araneaframework.uilib.form.formlist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.araneaframework.uilib.form.formlist.FormListModel;

public class MapFormListModel implements FormListModel {
  private Map<?, Object> map;
  
  public MapFormListModel(Map<?, Object> map) {
    this.map = map;
  }
  
  public List<Object> getRows() {
    return new ArrayList<Object>(map.values());
  }
}
