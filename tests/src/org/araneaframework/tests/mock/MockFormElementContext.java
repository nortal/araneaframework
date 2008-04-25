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

package org.araneaframework.tests.mock;

import java.util.HashSet;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MockFormElementContext implements FormElementContext {

	private Set errors;

	private Environment environment;

	private String label;

	private boolean mandatory;

	private boolean disabled;

	private boolean read;

	private Object value;

	public MockFormElementContext() {
	}

	public MockFormElementContext(String label, boolean mandatory,
			boolean disabled) {
		this.label = label;
		this.mandatory = mandatory;
		this.disabled = disabled;
	}

	public boolean isValid() {
		return (errors == null || errors.size() == 0);
	}

	public void addError(String error) {
		getErrors().add(error);
	}

	public void addErrors(Set errors) {
		getErrors().addAll(errors);
	}

	public void clearErrors() {
		errors.clear();
	}

	public Environment getEnvironment() {
		return environment;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Set getErrors() {
		if (errors == null)
			errors = new HashSet();
		return errors;
	}

	public Control getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	public Converter getConverter() {
		// TODO Auto-generated method stub
		return null;
	}

}
