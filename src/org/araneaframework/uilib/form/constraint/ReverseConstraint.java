package org.araneaframework.uilib.form.constraint;

import java.util.Collection;
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
 * 
 * @since 1.1
 */
public class ReverseConstraint extends  BaseConstraint {
	protected Set localErrors = new HashSet();
	protected Constraint toReverse;
	protected ReverseConstraintErrorMessageFactory errorMessageFactory;

	/**
	 * @param toReverse {@link Constraint} to reverse
	 */
	public ReverseConstraint(Constraint toReverse) {
		this.toReverse = toReverse;
	}

	/**
	 * @param toReverse {@link Constraint} to reverse
	 * @param customErrorMessage error message to produce when contained constraint <i>validates</i>.
	 */
	public ReverseConstraint(Constraint toReverse, String customErrorMessage) {
		this(toReverse);
		setCustomErrorMessage(customErrorMessage);
	}
	
	public ReverseConstraint(Constraint toReverse, ReverseConstraintErrorMessageFactory errorMessageFactory) {
		this(toReverse);
		this.errorMessageFactory = errorMessageFactory;
	}

	/**
	 * @return {@link Constraint} reversed by this {@link ReverseConstraint}  
	 */
	public Constraint getConstraint() {
		return toReverse;
	}
	
	/**
	 * Can be used to produce reverse constraint validation error message, where
	 * {@link ReverseConstraint#setCustomErrorMessage(String)} or {@link ReverseConstraint#ReverseConstraint(Constraint, String)}
	 * do not suffice.
	 * 
	 * @author Taimo Peelo (taimo@araneaframework.org)
	 */
	public static interface ReverseConstraintErrorMessageFactory {
		/**
		 * Should return validation errors for {@link ReverseConstraint} which did not validate.
		 * @param c ReverseConstraint which validation failed  
		 * @return Collection&lt;String&gt; with constraint validation errors.
		 */
		public Collection getErrorMessage(ReverseConstraint c);
	}

	protected void validateConstraint() throws Exception {
		toReverse.validate();
        Set errors = toReverse.getErrors();
        // Reverse constraint is invalid when wrapped constraint validates
        if (errors.isEmpty()) {
        	if (customErrorMessage != null) 
        		addError(customErrorMessage);
        	else if (errorMessageFactory != null)
        		addErrors(errorMessageFactory.getErrorMessage(this));
        	else
        		addError("Reverse constraint validation failed for constraint " + getConstraint() + ". No details available because custom validation error message was not set.");
        }
        toReverse.clearErrors();
	}

	public void clearErrors() {
		toReverse.clearErrors();
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
