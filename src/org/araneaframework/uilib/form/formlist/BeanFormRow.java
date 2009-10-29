package org.araneaframework.uilib.form.formlist;

import org.araneaframework.uilib.form.BeanFormWidget;


public class BeanFormRow<K, R> extends FormRow<K, R> {

  
  public BeanFormRow(BeanFormListWidget<K, R> formList, K rowKey, R row, String rowFormId, BeanFormWidget<R> formWidget, boolean open) {
    super(formList, rowKey, row, rowFormId, formWidget, open);
  }

  @Override
  @SuppressWarnings("unchecked")
  public BeanFormWidget<R> getForm() {
    return (BeanFormWidget<R>) super.getForm();
  }

  @Override
  public BeanFormListWidget<K, R> getFormList() {
    return (BeanFormListWidget<K, R>) super.getFormList();
  }
}
