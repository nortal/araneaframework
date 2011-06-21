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

package org.araneaframework.tests.mock;

import java.util.HashSet;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.framework.MessageContext.MessageData;
import org.araneaframework.framework.filter.StandardMessagingFilterWidget.StandardMessageData;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MockFormElementContext<C, D> implements FormElementContext<C, D> {

  private Set<MessageData> errors;

  private Environment environment;

  private String label;

  private boolean mandatory;

  private boolean disabled;

  private boolean read;

  private D value;

  public MockFormElementContext() {}

  public MockFormElementContext(String label, boolean mandatory, boolean disabled) {
    this.label = label;
    this.mandatory = mandatory;
    this.disabled = disabled;
  }

  public boolean isValid() {
    return (this.errors == null || this.errors.size() == 0);
  }

  public void addError(String error, Object... params) {
    getErrors().add(new StandardMessageData(error, params));
  }

  public void addErrors(Set<MessageData> errors) {
    getErrors().addAll(errors);
  }

  public void clearErrors() {
    this.errors.clear();
  }

  public Environment getEnvironment() {
    return this.environment;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public boolean isDisabled() {
    return this.disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isRead() {
    return this.read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public D getValue() {
    return this.value;
  }

  public void setValue(D value) {
    this.value = value;
  }

  public Set<MessageData> getErrors() {
    if (this.errors == null) {
      this.errors = new HashSet<MessageData>();
    }
    return this.errors;
  }

  public Control<C> getControl() {
    return null;
  }

  public Converter<C, D> getConverter() {
    return null;
  }

}
