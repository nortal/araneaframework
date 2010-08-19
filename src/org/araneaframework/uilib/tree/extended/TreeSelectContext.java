package org.araneaframework.uilib.tree.extended;

/**
 * Provides current highlighted element management along with a node handler.
 * Intended for use in {@link TreeSelectNodeDisplayWidget}
 * 
 * Highlighted element is the one that was the last one to fire selection event. 
 * Whoever uses this context should set some ID of upon selection event, so that
 * a certain node could be emphasized during rendering.
 * 
 * @author Jevgeni Võssotski
 *
 */
public interface TreeSelectContext {

  String getHighlightedElementId();
  
  void setHighlightedElementId(String elementId);
  
  TreeNodeHandler getTreeNodeHandler();
  
  String getPreselectedNodeKey();
  
  boolean shouldBeExpanded(String nodeKey);
}
