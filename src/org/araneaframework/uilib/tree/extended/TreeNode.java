package org.araneaframework.uilib.tree.extended;

import java.io.Serializable;

public class TreeNode implements Serializable {
  protected String key;
  protected String parentKey;
  protected boolean isSelectable = false;
  protected Object valueObject;

  public boolean isSelectable() {
    return isSelectable;
  }

  public void setIsSelectable(boolean isSelectable) {
    this.isSelectable = isSelectable;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object getValueObject() {
    return valueObject;
  }

  public void setValueObject(Object valueObject) {
    this.valueObject = valueObject;
  }

  public String getParentKey() {
    return parentKey;
  }

  public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }
}
