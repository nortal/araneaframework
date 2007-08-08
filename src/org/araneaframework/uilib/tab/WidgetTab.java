/**
* Copyright 2006 Webmedia Group Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**/
package org.araneaframework.uilib.tab;

import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Simple implementation of Tab that always returns new instance of class
 * passed to this tab as a constructor parameter.
 * 
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public class WidgetTab extends Tab {

  private Class clazz;

  /**
   * @param clazz should be a subclass of {@link Widget} with a no-arg constructor, or <code>IllegalArgumentException</code> is thrown
   * @throws IllegalArgumentException if parameter <code>clazz</code> does not represent a subclass of the {@link Widget} or
   * if this widget has no no-arg constructor
   */
  public WidgetTab(final String id, final String labelId, Class clazz) throws IllegalArgumentException {
    super(id, labelId);
    
    if(!Widget.class.isAssignableFrom(clazz)) {
      throw new IllegalArgumentException("Can create only widget subclasses!");
    }
    
    try {
      clazz.getConstructor(new Class[] {});
    } catch (SecurityException e) {
      throw ExceptionUtil.uncheckException(e);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
    
    this.clazz = clazz;
  }
  
  public Widget createWidget() {
    try {
      return (Widget) clazz.newInstance();
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

}
