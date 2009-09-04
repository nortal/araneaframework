package org.araneaframework.framework.context;

import java.util.Iterator;
import java.util.List;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.FlowContext.Handler;
import org.araneaframework.uilib.list.ListWidget;

/**
 * Refreshes <code>ListWidget</code>(s) on finish and/or on cancel.
 * 
 * @author Anton Kuzmin (anton.kuzmin@webmedia.ee)
 */
public class RefreshingListsFlowContextHandler implements Handler {

  private static final long serialVersionUID = 1L;

  protected List lists;

  protected boolean handleCancel;

  protected boolean handleFinish;

  /**
   * Refreshes lists on finishing and/or cancelling a flow.
   * 
   * @param lists <code>ListWidget</code>s to be refreshed.
   */
  public RefreshingListsFlowContextHandler(List lists) {
    this(true, true, lists);
  }

  /**
   * Refreshes lists on finish and also on cancel if apropriate parameters are
   * set to <true>true</code>.
   * 
   * @param handleCancel if <true>true</code>, the lists will be refreshed on
   *          cancel.
   * @param handleFinish if <true>true</code>, the lists will be refreshed on
   *          finish.
   * @param lists <code>ListWidget</code>s to be refreshed.
   */
  public RefreshingListsFlowContextHandler(boolean handleCancel,
      boolean handleFinish, List lists) {
    Assert.notNullParam(lists, "lists");

    this.handleCancel = handleCancel;
    this.handleFinish = handleFinish;
    this.lists = lists;
  }

  public void onFinish(Object returnValue) {
    if (this.handleFinish) {
      refreshLists();
    }
  }

  public void onCancel() {
    if (this.handleCancel) {
      refreshLists();
    }
  }

  protected void refreshLists() {
    for (Iterator i = this.lists.iterator(); i.hasNext();) {
      ListWidget list = (ListWidget) i.next();
      list.refresh();
    }
  }
}
