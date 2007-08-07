package org.araneaframework.uilib.tab;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Widget;

public abstract class Tab implements Serializable {

  private final String id;
  private final String labelId;
  private boolean enabled;
  private String tooltip;

  public Tab(final String id, final String labelId) {
    if (id == null || StringUtils.isBlank(id)) {
      throw new IllegalArgumentException("Cannot accept empty id!");
    }
    this.id = id;
    this.labelId = labelId;
    this.enabled = true;
  }

  /**
   * Each tab (or more commonly each tab subclass) is responsible for creating the widget that should be displayed when
   * that tab is selected.
   */
  public abstract Widget createWidget();

  public String getId() {
    return id;
  }

  public String getLabelId() {
    return labelId;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void disable() {
    this.enabled = false;
  }

  public void enable() {
    this.enabled = true;
  }

  public String toString() {
    return "Tab[id=" + id + "; labelId=" + labelId + "]";
  }

  public boolean equals(final Object o) {
    if (!(o instanceof Tab)) {
      return false;
    }
    final Tab t = (Tab) o;
    return this.id.equals(t.id);
  }

  public int hashCode() {
    return id.hashCode();
  }

  public void setTooltip(final String tooltip) {
    this.tooltip = tooltip;
  }

  public String getTooltip() {
    return tooltip;
  }
}
