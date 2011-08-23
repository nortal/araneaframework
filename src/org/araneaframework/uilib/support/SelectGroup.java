/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.uilib.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.araneaframework.core.util.Assert;

/**
 * A group of select items that itself cannot be selected, but can contain child options that can be selected.
 * <p>
 * A group can be disabled, which also disables access to child-options. Whether disabled or empty groups are rendered,
 * depends on the tag, however, not rendering is more recommended.
 * <p>
 * This class also provides a contract for hiding the group (OPTGROUP) tag while its child options still get rendered.
 * When the default (empty) constructor is used for instantiating select group or when {@link #NO_GROUP} is used as the
 * label, the group tag won't be rendered. For convenience, this class also provides method {@link #isNoGroup()} to
 * check whether group tag should be omitted.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 2.0
 */
public class SelectGroup<T> implements Serializable {

  /**
   * The identifier-label used for identifying whether the group is default (no label) and its content must be rendered
   * without OPTGROUP HTML tag.
   */
  public static final String NO_GROUP = "$no_group$";

  /**
   * A collection of child options belonging to this group.
   */
  private List<T> childOptions = new ArrayList<T>(50);

  private String label;

  private boolean disabled;

  /**
   * Creates a new select control items "invisible" group with no label. The "invisible" means that the group (OPTGROUP)
   * won't be rendered, but its child-options (OPTION) will be rendered.
   * 
   * @see #isNoGroup()
   * @see #NO_GROUP
   */
  public SelectGroup() {
    this(NO_GROUP);
  }

  /**
   * Creates a new select control items group with given label and with no child-options.
   * 
   * @param label The label for this group.
   * @see #isNoGroup()
   * @see #NO_GROUP
   */
  public SelectGroup(String label) {
    this.label = label;
  }

  /**
   * Creates a new select control items group with given label and with given child-options.
   * 
   * @param label The label for this group.
   * @param options The options that this group will contain, must not be <code>null</code>.
   * @see #isNoGroup()
   * @see #NO_GROUP
   */
  public SelectGroup(String label, List<T> options) {
    this(label);
    addOptions(options);
  }

  /**
   * Provides whether the content of this group must be rendered without the OPTGROUP HTML tag. The content will still be rendered, unless this group is disabled.
   * 
   * @return A Boolean that is <code>true</code> when the OPTGROUP HTML tag must not be used when rendering this tag.
   */
  public boolean isNoGroup() {
    return NO_GROUP.equals(this.label);
  }

  /**
   * Provides the label of this group. Beware that the returned label may be the value of {@link #NO_GROUP} in which
   * case the group must not be rendered.
   * 
   * @return The label of the group.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Changes the disabled status of this group.
   * <p>
   * Changing the status to disabled also disables child options, although the underlying child option object might not
   * know it. Vice versa, all child items may be set to disabled, while the group may be enabled, yet no option can be
   * selected. When group is rendered, this disabled status should be used only on the OPTGROUP element, and group child
   * item disabled status should be used on the OPTION element.
   * 
   * @param disabled A Boolean that is <code>true</code> when the group should be set to disabled status, otherwise
   *          <code>true</code>.
   */
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  /**
   * Provides the disabled status of this group.
   * <p>
   * When the status is set to disabled then child options are also disabled, although the underlying child option
   * objects might not know it. Vice versa, all child items may be set to disabled, while the group may be enabled,
   * yet no option can be selected. When group is rendered, this disabled status should be used only on the OPTGROUP
   * element, and group child item disabled status should be used on the OPTION element.
   * 
   * @return A Boolean that is <code>true</code> when the group is in disabled status, otherwise <code>true</code>.
   */
  public boolean isDisabled() {
    return this.disabled;
  }

  /**
   * Removes all child options of the group.
   * 
   * @return Reference to the group where all child options were removed.
   */
  public SelectGroup<T> clear() {
    this.childOptions.clear();
    return this;
  }

  /**
   * Provides whether the group is empty (contains no child option at all).
   * 
   * @return A Boolean that is <code>true</code> when the group contains no child option.
   */
  public boolean isEmpty() {
    return this.childOptions.isEmpty();
  }

  /**
   * Appends an option to existing child options.
   * 
   * @param childOption The option to add, must not be <code>null</code>.
   * @return Reference to the group where the child option was added.
   */
  public SelectGroup<T> addOption(T childOption) {
    Assert.notNullParam(childOption, "childOption");
    this.childOptions.add(childOption);
    return this;
  }

  /**
   * Appends given options to existing child options.
   * 
   * @param childOptions The options to add, must not be <code>null</code>.
   * @return Reference to the group where the options were added.
   */
  public SelectGroup<T> addOptions(Collection<? extends T> childOptions) {
    Assert.notNullParam(childOptions, "childOptions");
    this.childOptions.addAll(childOptions);
    return this;
  }

  /**
   * Sets the child options that this group will contain.
   * 
   * @param childOptions The child options that the group must contain, must not be <code>null</code> (use
   *          {@link #clear()} when removing all items is needed).
   * @return Reference to the group where the options were set.
   * @see #clear()
   */
  public SelectGroup<T> setOptions(List<T> childOptions) {
    Assert.notNullParam(childOptions, "childOptions");
    this.childOptions = childOptions;
    return this;
  }

  /**
   * Provides select items of this group.
   * 
   * @return An unmodifiable list of all child items belonging to this group.
   */
  public List<T> getOptions() {
    return Collections.unmodifiableList(this.childOptions);
  }

  /**
   * Sorts all the child options as defined by the comparator.
   * 
   * @param comparator The comparator used for sorting child options.
   */
  public void sort(Comparator<T> comparator) {
    Assert.notNullParam(comparator, "comparator");
    Collections.sort(this.childOptions, comparator);
  }
}
