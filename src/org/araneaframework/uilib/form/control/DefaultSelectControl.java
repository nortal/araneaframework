package org.araneaframework.uilib.form.control;

import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * Concrete type ({@link DisplayItem}) implementation for {@link SelectControl}. This means that unlike SelectControl
 * that stores its data in the type specified, this sub implementation stores data only in {@link DisplayItem}s. This
 * implementation is additionally needed as <code>DisplayItem</code>s don't have all getters and setters that are
 * required when adding new items through <code>SelectControl</code>.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class DefaultSelectControl extends SelectControl<DisplayItem> {

  public DefaultSelectControl() {
    super(DisplayItem.class, "label", "value");
  }

  @Override
  public void addItem(String label, String value) {
    Assert.notNullParam(label, "label");
    try {
      DisplayItem newItem = new DisplayItem(value, label);
      DisplayItemUtil.assertUnique(this.items, newItem);
      this.items.add(newItem);
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  protected class ViewModel extends SelectControl<DisplayItem>.ViewModel {

    @Override
    public String getControlType() {
      return SelectControl.class.getSimpleName();
    }
  }
}
