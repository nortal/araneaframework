package org.araneaframework.uilib.form.constraint;

import org.araneaframework.uilib.form.formlist.BaseFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListWidget;
import org.araneaframework.uilib.form.formlist.FormRow;

/**
 * A base constraint for constraints that need to process {@link FormListWidget} rows in order to determine whether the
 * constraint is fulfilled. When implementations are validating one of the rows being iterated, they should also check
 * that the row being validated does not match the other row.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public abstract class BaseFormListRowsFieldConstraint<C, D, K, R> extends BaseFieldConstraint<C, D> {

  private BaseFormListWidget<K, R> formList;

  public BaseFormListRowsFieldConstraint(BaseFormListWidget<K, R> formList) {
    this.formList = formList;
  }

  @Override
  protected final void validateConstraint() {
    for (FormRow<K, R> formRow : this.formList.getFormRows().values()) {
      if (formRow.isOpen() && !validateOpenFormRow(formRow)) {
        break;
      } else if (!formRow.isOpen() && !validateClosedFormRow(formRow)) {
        break;
      }
    }
  }

  /**
   * Checks constraint requirement(s) on an opened (for editing) form list row.
   * 
   * @param formRow Form list row to be processed.
   * @return A Boolean where <code>true</code> value indicates that form list rows processing can continue.
   */
  protected abstract boolean validateOpenFormRow(FormRow<K, R> formRow);

  /**
   * Checks constraint requirement(s) on a closed (for editing) form list row.
   * 
   * @param formRow Form list row to be processed.
   * @return A Boolean where <code>true</code> value indicates that form list rows processing can continue.
   */
  protected abstract boolean validateClosedFormRow(FormRow<K, R> formRow);
}
