package org.araneaframework.uilib.form.converter;

import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.control.BaseSelectControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * @author Aleksandr Koltakov
 */
public class LongToDisplayItemConverter extends BaseConverter<Object, Object> {
  
  @Override
  public Converter<Object, Object> newConverter() {
    return new LongToDisplayItemConverter();
  }
 
  @Override
  protected Object convertNotNull(Object data) {
    return Long.valueOf(((DisplayItem) data).getValue());
  }
 
  @Override
  @SuppressWarnings("unchecked")
  protected Object reverseConvertNotNull(Object data) {
    BaseSelectControl<DisplayItem, ?> control = (BaseSelectControl<DisplayItem, ?>) getFormElementCtx().getControl();
    return DisplayItemUtil.getSelectedItemByValue(control.getEnabledItems(), ObjectUtils.toString(data));    
  }
}

