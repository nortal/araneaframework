package org.araneaframework.uilib.support;

import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Vassili Jakovlev (vassili@webmedia.ee)
 */
public interface DisplayItemBuilder<T> {

  public DisplayItem buildDisplayItem(T t);

}
