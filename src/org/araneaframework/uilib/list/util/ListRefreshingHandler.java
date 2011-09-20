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

package org.araneaframework.uilib.list.util;

import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.FlowContext.Handler;
import org.araneaframework.uilib.list.ListWidget;

/**
 * Refreshes <code>ListWidget</code>(s) on finish and/or on cancel.
 * 
 * @author Anton Kuzmin (anton.kuzmin@webmedia.ee)
 */
public class ListRefreshingHandler implements Handler<Object> {

  public enum Handle {
    CANCEL, FINISH, ALL;
  }

  protected ListWidget<?>[] lists;

  protected boolean handleCancel;

  protected boolean handleFinish;

  /**
   * Refreshes lists on finishing and/or canceling a flow.
   * 
   * @param lists <code>ListWidget</code>s to be refreshed.
   */
  public ListRefreshingHandler(ListWidget<?>... lists) {
    this(true, true, lists);
  }

  /**
   * Refreshes lists on finish and also on cancel if appropriate parameters are set to <code>true</code>.
   * 
   * @param handleCancel if <code>true</code>, the lists will be refreshed on cancel.
   * @param handleFinish if <code>true</code>, the lists will be refreshed on finish.
   * @param lists <code>ListWidget</code>s to be refreshed.
   */
  public ListRefreshingHandler(boolean handleCancel, boolean handleFinish, ListWidget<?>... lists) {
    Assert.notNullParam(lists, "lists");
    this.handleCancel = handleCancel;
    this.handleFinish = handleFinish;
    this.lists = lists;
    Assert.notNullParam(this, lists, "lists");
  }

  /**
   * Refreshes lists on finish and/or on cancel depending on the <code>handle</code> parameter.
   * 
   * @param handle The enum value that describes, in which case the lists will be refreshed.
   * @param lists <code>ListWidget</code>s to be refreshed.
   * @since 2.0
   */
  public ListRefreshingHandler(Handle handle, ListWidget<?>... lists) {
    this(handle != Handle.FINISH, handle != Handle.CANCEL, lists);
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

  /**
   * Refreshes the provided lists.
   */
  protected void refreshLists() {
    for (ListWidget<?> list : this.lists) {
      list.refresh();
    }
  }
}
