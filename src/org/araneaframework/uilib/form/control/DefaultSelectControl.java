package org.araneaframework.uilib.form.control;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.DisplayItemBuilder;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * Concrete type ({@link DisplayItem}) implementation for {@link SelectControl}. This means that unlike SelectControl
 * that stores its data in the type specified, this sub implementation stores data only in {@link DisplayItem}s. This
 * implementation is additionally needed as <code>DisplayItem</code>s don't have all getters and setters that are
 * required when adding new items through <code>SelectControl</code>.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @author Vassili Jakovlev (vassili@webmedia.ee)
 * @since 2.0
 */
public class DefaultSelectControl extends SelectControl<DisplayItem> {

  public DefaultSelectControl() {
    super(DisplayItem.class, "label", "value");
  }

  public <V> void addItems(Collection<V> values, DisplayItemBuilder<V> itemBuilder) {
    List<DisplayItem> displayItems = DisplayItemUtil.buildDisplayItems(values, itemBuilder);
    addItems(displayItems);
  }

  public <V> void addItems(Collection<V> values, DisplayItemBuilder<V> itemBuilder, Comparator<V> comparator) {
    List<DisplayItem> displayItems = DisplayItemUtil.buildOrderedDisplayItems(values, itemBuilder, comparator);
    addItems(displayItems);
  }

  @Override
  public void addItem(String label, String value) {
    Assert.notNullParam(label, "label");
    DisplayItem newItem = new DisplayItem(value, label);
    DisplayItemUtil.assertUnique(this.items, newItem);
    this.items.add(newItem);
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  protected class ViewModel extends SelectControl<DisplayItem>.ViewModel {

    @Override
    protected void initItems() {
      for (DisplayItem item : DefaultSelectControl.this.items) {
        boolean isDisabled = DefaultSelectControl.this.disabledItems.contains(item);
        this.selectItems.add(item);
        if (isDisabled) {
          item.setDisabled(isDisabled);
          this.disabledItems.add(item);
        } else {
          this.enabledItems.add(item);
        }
      }
    }

    @Override
    public String getControlType() {
      return SelectControl.class.getSimpleName();
    }
  }
}
