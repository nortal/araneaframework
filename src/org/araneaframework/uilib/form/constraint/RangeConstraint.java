/**
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
**/

package org.araneaframework.uilib.form.constraint;

import java.text.Collator;
import org.araneaframework.Environment;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;

/**
 * Given two form elements checks that their values are one after another.
 * It assumes that the values of both form elements are of the same type and comparable.
 * 
 * TODO: Add locale support for string ranges.
 * XXX: this hurts ...
 * 
 * @author <a href="mailto:kt@webmedia.ee">Konstantin Tretyakov</a>
 */
public final class RangeConstraint extends BaseConstraint {

	protected boolean allowEquals;
	protected FormElement fieldLo, fieldHi;

	/**
	 * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or null)
	 * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or null)
	 * @param allowEquals If this is true, the constraint will be considered satisfied when values of fieldLo and fieldHi are 
	 *                    equal. Otherwise the constraint won't be satisfied in this case.
	 * @param locale  In case the data to be compared is of type String, this locale will be used in comparison.
	 */
	public RangeConstraint(FormElement fieldLo, FormElement fieldHi, boolean allowEquals) {
		this.allowEquals = allowEquals;
		this.fieldHi = fieldHi;
		this.fieldLo = fieldLo;
	}

  protected void validateConstraint() {
    Object valueLo = fieldLo.getData().getValue();
    Object valueHi = fieldHi.getData().getValue();
    
    // If any of the values is null, we stay quiet no matter what.
    if (valueLo == null || valueHi == null) return;
    
    boolean loExtendsHi;
    
    if (valueHi.getClass().isAssignableFrom(valueLo.getClass()))
      loExtendsHi = true;
    else if (valueLo.getClass().isAssignableFrom(valueHi.getClass()))
      loExtendsHi = false;
    else
      throw new AraneaRuntimeException("RangeConstraint can be used only with fields of compatible types.");
    
    int comparison = 0;  // Will be -1, 0 or 1 depending on whether sLo is <, = or > than sHi 
    
    // Strings are handled separately because we have to compare them in given locale.
    if (valueLo instanceof String && valueHi instanceof String) {
      Collator collator = Collator.getInstance(); // TODO: Must be locale-specific
      comparison = collator.compare((String)valueLo, (String)valueHi);
    }
    else if (valueLo instanceof Comparable && valueHi instanceof Comparable){     
      if (loExtendsHi)
        comparison = ((Comparable)valueLo).compareTo(valueHi);
      else
        comparison = -1 * ((Comparable)valueHi).compareTo(valueLo);
    }
    else { // Objects are not comparable
      throw new AraneaRuntimeException("RangeConstraint expects fields of Comparable type");
    }
    
    if (comparison > 0 || (!allowEquals && comparison == 0)){
      addError(
          ErrorUtil.localizeAndFormat(
            UiLibMessages.RANGE_CHECK_FAILED, 
            t(fieldLo.getLabel(), fieldLo.getEnvironment()),
            t(fieldHi.getLabel(), fieldHi.getEnvironment()),
            getEnvironment()));
    }
  }
  
  private String t(String key, Environment env) {
	    LocalizationContext locCtx = 
	     (LocalizationContext) env.getEntry(LocalizationContext.class);
	    return locCtx.localize(key);
	  }

}
