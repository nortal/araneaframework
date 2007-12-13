package org.araneaframework.uilib.form.constraint;

import java.util.HashSet;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.uilib.form.Constraint;

/**
 * Constraint that reverses the contained {@link Constraint}&mdash;
 * validating only when contained {@link Constraint} is invalid.
 * When contained {@link Constraint} is valid, error message
 * set with {@link Constraint#setCustomErrorMessage(String)} is produced.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ReverseConstraint extends  BaseConstraint {
	protected Set localErrors = new HashSet();
	protected Constraint toReverse;

	public ReverseConstraint(Constraint toReverse) {
		this.toReverse = toReverse;
	}
	
	public ReverseConstraint(Constraint toReverse, String customErrorMessage) {
		this(toReverse);
		setCustomErrorMessage(customErrorMessage);
	}

	protected void validateConstraint() throws Exception {
		toReverse.validate();
        Set errors = toReverse.getErrors();
        if (errors.isEmpty()) {
        	addError(customErrorMessage);
        }
        toReverse.clearErrors();
	}

	public void clearErrors() {
		localErrors.clear();
	}
	
	protected void addError(String s) {
		localErrors.add(s);
	}

	public Set getErrors() {
		return localErrors;
	}

	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		toReverse.setEnvironment(environment);
	}
}
