package org.araneaframework.uilib.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Select group designed for {@link DisplayItem}s.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 2.0
 */
public class DisplayItemGroup extends SelectGroup<DisplayItem> {

  /**
   * Creates a new display items "invisible" group with no label. The "invisible" means that the group (OPTGROUP)
   * won't be rendered, but its child-options (OPTION) will be rendered.
   * 
   * @see #isNoGroup()
   * @see #NO_GROUP
   */
  public DisplayItemGroup() {
  }

  /**
   * Creates a new display items group with given label and with no child-options.
   * 
   * @param label The label for this group.
   * @see #isNoGroup()
   * @see #NO_GROUP
   */
  public DisplayItemGroup(String label) {
    super(label);
  }

  /**
   * Creates a new display items group with given label and with given child-options.
   * 
   * @param label The label for this group.
   * @param options The options that this group will contain, must not be <code>null</code>.
   * @see #isNoGroup()
   * @see #NO_GROUP
   */
  public DisplayItemGroup(String label, List<DisplayItem> options) {
    super(label, options);
  }

  /**
   * Provides whether the group contains enabled items.
   * 
   * @return A Boolean that is <code>true</code> when the group contains no enabled child option.
   */
  public boolean isEnabledEmpty() {
    return isEmpty() || getEnabledOptions().isEmpty();
  }

  /**
   * Provides enabled only child options. The returned list is modifiable but it cannot be used to alter the disabled
   * status of options.
   * 
   * @return The list of enabled child options.
   */
  public List<DisplayItem> getEnabledOptions() {
    List<DisplayItem> enabledOptions = new ArrayList<DisplayItem>(super.getOptions().size());

    for (Iterator<DisplayItem> i = super.getOptions().iterator(); i.hasNext(); ) {
      DisplayItem item = i.next();
      if (!item.isDisabled()) {
        enabledOptions.add(item);
      }
    }

    return enabledOptions;
  }
}
