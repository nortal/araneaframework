package org.araneaframework.uilib.tree.extended;

import java.io.Serializable;

/**
 * Determines how a node in a selectable tree is displayed and 
 * how its selection is handled (if the node is selectable).
 * 
 * @author Jevgeni Võssotski
 */
public interface TreeNodeHandler extends Serializable {

  /**
   * @param valueObject inner object from {@link TreeNode}
   * @return String representation of value object
   */
  public String getDisplayString(Object valueObject);
  
  /**
   * @param valueObject inner object from {@link TreeNode}
   */
  public void handleEventSelect(Object valueObject);
  
}
