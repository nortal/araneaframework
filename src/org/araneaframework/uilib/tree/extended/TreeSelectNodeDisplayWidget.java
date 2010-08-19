package org.araneaframework.uilib.tree.extended;

import java.io.PrintWriter;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.EventListener;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.tree.TreeNodeContext;

/**
 * Display widget for a node in a selectable tree.
 * Draws a string returned by {@link TreeNodeHandler#getDisplayString(Object)} and, if the
 * node is selectable, puts a link tag around it, and registers event for it, so that 
 * {@link TreeNodeHandler} is notified of selection event.
 * @author Jevgeni Võssotski
 */
public class TreeSelectNodeDisplayWidget extends BaseApplicationWidget {
  protected TreeNode treeNode;
  private TreeNodeHandler treeNodeHandler;
  private final String selectEventId = "select";
  private final TreeSelectContext treeSelectContext;
  
  public TreeSelectNodeDisplayWidget(TreeNode treeNode, TreeSelectContext treeSelectContext) {
    this.treeNode = treeNode;
    this.treeSelectContext = treeSelectContext;
    treeNodeHandler = treeSelectContext.getTreeNodeHandler();
  }

  public void init() throws Exception {
    setGlobalEventListener(new ProxyEventListener(this));
    addEventListener(selectEventId, new EventListener() {
    
      public void processEvent(String eventId, InputData input) throws Exception {
        treeSelectContext.setHighlightedElementId(getElementId());
        treeNodeHandler.handleEventSelect(treeNode.getValueObject());
      }
    });
    
    if (treeNode.getKey().equals(treeSelectContext.getPreselectedNodeKey())) {
      treeSelectContext.setHighlightedElementId(getElementId());
    }
    getTreeNodeCtx().setCollapsed(!treeSelectContext.shouldBeExpanded(treeNode.getKey()));
  }

  public String getKey() {
    return treeNode.getKey();
  }

  public String getParentKey() {
    return treeNode.getParentKey();
  }

  protected TreeNodeContext getTreeNodeCtx() {
    return (TreeNodeContext) getEnvironment().getEntry(TreeNodeContext.class);
  }

  private String getElementId() {
    return getScope().toString();
  }
  
  public void render(OutputData output) throws Exception {
    HttpOutputData httpOutputData = (HttpOutputData) output; // watch out, nobody guarantees this will always succeed.
    PrintWriter out = httpOutputData.getWriter();

    // write contents inside TD, see also TreeSelectRenderer
    JspUtil.writeStartTag(out, "td");

    boolean selectable = treeNode.isSelectable();

    if (selectable) {
      JspUtil.writeOpenStartTag(out, "a");
      JspUtil.writeAttribute(out, "href", "#");

      UiUpdateEvent updateEvent =
          new UiUpdateEvent(selectEventId,
                            getScope().toString(),
                            null);
      JspUtil.writeEventAttributes(out, updateEvent);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
      JspUtil.writeAttribute(out, "id", getElementId());
      if (getElementId().equals(treeSelectContext.getHighlightedElementId())) {
        JspUtil.writeAttribute(out, "style", "{ text-decoration: underline; }");
      }
      JspUtil.writeCloseStartTag(out);
    }

    out.write(treeNodeHandler.getDisplayString(treeNode.getValueObject()));

    if (selectable) {
      JspUtil.writeEndTag(out, "a");
    }
    
    JspUtil.writeEndTag(out, "td");
    
    JspUtil.writeEndTag(out, "tr");
  }
}