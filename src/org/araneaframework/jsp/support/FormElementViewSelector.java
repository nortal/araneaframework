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

package org.araneaframework.jsp.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.araneaframework.jsp.tag.uilib.form.element.AutomaticTagFormElementTag;
import org.araneaframework.jsp.util.AutomaticFormElementUtil;

/**
 * A class that holds information about the tag and the attributes of the tag
 * that will be used to render the form element. It is mainly used
 * {@link AutomaticFormElementUtil}
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see AutomaticFormElementUtil
 * @see AutomaticTagFormElementTag
 */
public class FormElementViewSelector implements Serializable {

  /**
   * The key that can be used to put or get a selector (this class) from the
   * properties list of a view model belonging to a form element.
   * 
   * @see org.araneaframework.uilib.form.FormElement.ViewModel#getProperties()
   */
  public static final String FORM_ELEMENT_VIEW_SELECTOR_PROPERTY = "form.element.view.selector";

  /**
   * Default URI of the taglib from where the specified <code>tag</code> will
   * be searched.
   */
  public static final String DEFAULT_URI = "http://araneaframework.org/tag-library/standard";

  /**
   * The name of the tag that will be used to render the form element.
   */
  protected String tag;

  /**
   * The attributes for the tag are key-value pairs where key is a
   * <code>String</code> (the name of the attribute) and value is any object
   * (the value for the attribute).
   */
  protected Map<String, Object> attributes;

  /**
   * The URI of the tag library where the <code>tag</code> is located.
   */
  protected String uri;

  /**
   * Creates a view selector for a form element (the latter is specified through
   * {@link AutomaticFormElementUtil}). This object will only carry information
   * about which tag (with attributes) to use.
   * <p>
   * The attributes for the tag are key-value pairs where key is a
   * <code>String</code> (the name of the attribute) and value is any object
   * (the value for the attribute).
   * <p>
   * The attribute names must qualify to appropriate setter of class
   * {@link AutomaticTagFormElementTag} as:
   * <pre><code>set[attributeName]([AttributeValueType] attributeValue)</code></pre>
   * <p>
   * <b>This constructor expects that the URI of the tag library is the same as
   * for Aranea tag library!</b>
   * 
   * @param tagName The name of the tag (not a tag class name!) to use.
   * @param attributes The attributes for the tag.
   * @see AutomaticFormElementUtil
   * @see AutomaticTagFormElementTag
   */
  public FormElementViewSelector(String tagName, Map<String, Object> attributes) {
    this(DEFAULT_URI, tagName, attributes);
  }

  /**
   * Creates a view selector for a form element (the latter is specified through
   * {@link AutomaticFormElementUtil}). This object will only carry information
   * about which tag (with its URI and attributes) to use.
   * <p>
   * The <code>uri</code> is the URI of the tag.
   * <p>
   * The attributes for the tag are key-value pairs where key is a
   * <code>String</code> (the name of the attribute) and value is any object
   * (the value for the attribute).
   * <p>
   * The attribute names must qualifify to appropriate setter of class
   * {@link AutomaticTagFormElementTag} as:
   * <pre><code>set[attributeName]([AttributeValueType] attributeValue)</code></pre>
   * 
   * @param uri The URI of the tag library.
   * @param tag The name of the tag (not a tag class name!) to use.
   * @param attributes The attributes for the tag.
   * @see AutomaticFormElementUtil
   * @see AutomaticTagFormElementTag
   */
  public FormElementViewSelector(String uri, String tag, Map<String, Object> attributes) {
    this.uri = uri;
    this.tag = tag;
    if(attributes == null) {
      this.attributes =Collections.emptyMap();
    } else {
      this.attributes = attributes;
    }
  }

  /**
   * Provides access to the attributes of the tag. The attributes are key-value
   * pairs where key is a <code>String</code> (the name of the attribute) and
   * value is any object (the value for the attribute).
   * <p>
   * The attribute names must qualifify to appropriate setter of class
   * {@link AutomaticTagFormElementTag} as:
   * <pre><code>set[attributeName]([AttributeValueType] attributeValue)</code></pre>
   * 
   * @return the attributes of the tag.
   * @see AutomaticTagFormElementTag
   */
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  /**
   * Gets the name of the tag that will be used to render the form element.
   * 
   * @return the name of the tag.
   */
  public String getTag() {
    return tag;
  }

  /**
   * Gets the URI of the tag library.
   * 
   * @return the URI of the tag library.
   */
  public String getUri() {
    return uri;
  }

}
