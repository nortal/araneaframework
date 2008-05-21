
package org.araneaframework.uilib.tab;

import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;

/**
 * This interface identifies a tab widget.
 * 
 * @see TabContainerContext
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.3
 */
public interface TabContext extends ApplicationWidget {

  /**
   * Enables this tab.
   */
  void enableTab();

  /**
   * Disables this tab.
   */
  void disableTab();

  /**
   * Deselects this tab.
   */
  void deleselectTab();

  /**
   * Gets the label of this tag.
   * 
   * @return the label of this tag.
   */
  String getLabel();

  /**
   * Gets the label widget of this tag.
   * 
   * @return the label widget of this tag.
   */
  Widget getLabelWidget();

  /**
   * Gets the tab content widget.
   * 
   * @return the tab content widget.
   */
  Widget getTabContentWidget();

  /**
   * Gets whether this tab is disabled.
   * 
   * @return <code>true</code>, if this tab is disabled.
   */
  boolean isTabDisabled();

  /**
   * Gets whether this tab is selected.
   * 
   * @return <code>true</code>, if this tab is selected.
   */
  boolean isSelected();

  /**
   * Gets whether this tab is stateless.
   * 
   * @return <code>true</code>, if this tab is stateless.
   */
  boolean isStateless();
}
