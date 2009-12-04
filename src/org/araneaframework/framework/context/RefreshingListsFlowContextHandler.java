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

package org.araneaframework.framework.context;

import java.util.List;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.FlowContext.Handler;
import org.araneaframework.uilib.list.ListWidget;

/**
 * Refreshes <code>ListWidget</code>(s) on finish and/or on cancel.
 * 
 * @author Anton Kuzmin (anton.kuzmin@webmedia.ee)
 */
public class RefreshingListsFlowContextHandler<T> implements Handler<T> {

  protected List<ListWidget<T>> lists;

  protected boolean handleCancel;

  protected boolean handleFinish;

  /**
   * Refreshes lists on finishing and/or canceling a flow.
   * 
   * @param lists <code>ListWidget</code>s to be refreshed.
   */
  public RefreshingListsFlowContextHandler(List<ListWidget<T>> lists) {
    this(true, true, lists);
  }

  /**
   * Refreshes lists on finish and also on cancel if appropriate parameters are set to <true>true</code>.
   * 
   * @param handleCancel if <true>true</code>, the lists will be refreshed on cancel.
   * @param handleFinish if <true>true</code>, the lists will be refreshed on finish.
   * @param lists <code>ListWidget</code>s to be refreshed.
   */
  public RefreshingListsFlowContextHandler(boolean handleCancel, boolean handleFinish, List<ListWidget<T>> lists) {
    Assert.notNullParam(lists, "lists");
    this.handleCancel = handleCancel;
    this.handleFinish = handleFinish;
    this.lists = lists;
  }

  public void onFinish(T returnValue) {
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
    for (ListWidget<T> list : this.lists) {
      list.refresh();
    }
  }
}
