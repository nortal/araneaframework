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

package org.araneaframework.jsp.tag.uilib.form;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.basic.BaseKeyboardHandlerTag;
import org.araneaframework.jsp.tag.basic.KeyboardHandlerHtmlTag;

/**
 * Invokes a "uiRegisterKeyboardHandler" javascript. This is basically the same stuff as
 * {@link org.araneaframework.jsp.tag.basic.KeyboardHandlerHtmlTag KeyboardHandlerHtmlTag} with a few modifications:
 * <ul>
 * <li>There is no <code>scope</code> attribute. Instead, the tag assumes that it is located inside a form, and takes
 * the full id of that form as its scope.</li>
 * <li>As an alternative to specifying the <code>handler</code> attribute, you may specify a form element and a
 * javascript event to invoke on that element. You specify the element by its id relative to the surrounding form. The
 * event is given as a name of the javascript function to be invoked on the element. For example, if you specify the
 * element as "someButton", and event as "click", then when the required keyboard event occurs, the following javascript
 * will be executed:
 * <pre>
 * var el = $(&quot;&lt;form-id&gt;.someButton&quot;);
 * el.click();</pre>
 * </li>
 * </ul>
 * 
 * @see org.araneaframework.jsp.tag.basic.KeyboardHandlerHtmlTag
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 * 
 * @jsp.tag
 *  name = "formKeyboardHandler"
 *  body-content = "empty"
 *  description = "Registers a 'form' keyboard handler. Similar to keyboardHandler, but here the scope is relative to the form inside which the tag is located, and instead of specifying the handler, you may give the id of an element, and a javascript event to call on that element."
 */
public class FormKeyboardHandlerHtmlTag extends BaseKeyboardHandlerTag {

  protected String handler;

  protected String subscope;

  protected String elementId;

  protected String fullElementId;

  protected String event;

  /**
   * @see KeyboardHandlerHtmlTag#setHandler
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "A javascript handler function that takes two parameters - the event object and the element ID for which the event was fired. Example:<br/>function(event, elementId) { alert(elementId); }<br/>Either handler or elementId/event pair should be specified, not both."
   */
  public void setHandler(String handler) {
    this.handler = evaluate("handler", handler, String.class);
  }

  /**
   * Specifies form element which is the scope of this handler. By default the "scope" (as in
   * {@link KeyboardHandlerHtmlTag}) of this keyboard handler is the form inside which the handler is defined. By
   * specifying this string you may narrow the scope to a certain element. For example if the handler is defined inside
   * form "myForm", and you specify sub-scope as "myElement", the scope of the handler will be "myForm.myelement", no the
   * default "myForm".
   * 
   * @see KeyboardHandlerHtmlTag#setScope(String)
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "By default, the scope of this keyboard handler is defined by the form inside which it is located. You may append a suffix to that scope by using this attribute. E.g. if you set subscope='someElement', then the active scope of the keyboard handler will be 'someForm.someElement', not 'someForm' as would be by default. The handler will therefore be active only for the element 'someElement'."
   */
  public void setSubscope(String subscope) {
    this.subscope = evaluate("subscope", subscope, String.class);
  }

  /**
   * Set the (relative) id of the element, whose javascript event should be invoked. Either you specify this property,
   * or the fullElementId property, not both. <br>
   * The two attributes are here because there are currently two kinds of "button" tags in Jsp-Ui. One is a
   * <code>&lt;button&gt;</code> tag that corresponds to a form element. This tag has an "id" attribute, and may look
   * like
   * <pre>&lt;button id=&quot;myButton&quot;/&gt;</pre>
   * The HTML <code>id</code> attribute of the corresponding input element will be something like "myForm.myButton". <br>
   * Therefore, if you would like to bind a keyboard handler to a "button", use the elementId attribute, and the tag
   * will automatically append the correct prefix.
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Set the (relative) ID of the element, whose javascript event should be invoked. The ID is relative with respect to the surrounding form. Instead of this attribute, you may set element's full id using the fullElementId attribute, but you can't set both attributes at once."
   */
  public void setElementId(String elementId) {
    this.elementId = evaluate("elementId", elementId, String.class);
  }

  /**
   * @see #setElementId
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Set the (relative) ID of the element, whose javascript event should be invoked. The ID is relative with respect to the surrounding form. Instead of this attribute, you may set element's full id using the fullElementId attribute, but you can't set both attributes at once."
   */
  public void setFullElementId(String fullElementId) {
    this.fullElementId = evaluate("fullElementId", fullElementId, String.class);
  }

  /**
   * @throws JspException 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Set the javascript event that should be invoked. "click" and "focus" are safe for most controls. If target element (the one given by elementId) is a select-box, you may also set "select". For more, consult the javascript reference. This attribute is not foolproof and you may produce invalid javascript if you don't know what you are doing. "onclick" is the default."
   */
  public void setEvent(String eventId) throws JspException {
    this.event = evaluateNotNull("event", eventId, String.class);
  }

  //
  // Implementation
  //
  @Override
  protected final int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    this.handler = StringUtils.defaultIfEmpty(this.handler, null);
    this.fullElementId = StringUtils.defaultIfEmpty(this.fullElementId, null);
    this.event = StringUtils.defaultIfEmpty(this.event, null);

    validateInput();

    if (this.handler == null) {
      // If elementId was given, translate to fullElementId
      if (this.elementId != null) {
        this.fullElementId = elementIdToFullElementId(this.pageContext, this.elementId);
      }

      this.handler = createHandlerToInvokeJavascriptEvent(this.fullElementId, this.event);
    }

    // Write out.
    writeKeypressHandlerScript(out, getTargetScope(), this.handler);

    return SKIP_BODY;
  }

  protected void validateInput() throws JspException {
    if (StringUtils.isBlank(this.handler)) {
      
      // One of elemenId/event must be specified
      if (this.event == null || this.elementId == null && this.fullElementId == null) {
        throw new JspException("You must specify handler / elementId & event for FormKeyboardHandlerHtmlTag (elementId="
            + this.elementId + ", fullElementId=" + this.fullElementId + ", event=" + this.event + ",subscope="
            + this.subscope);
      }

      // Only one of elementId/fullElementId must be specified
      if ((this.elementId == null) == (this.fullElementId == null)) {
        throw new JspException("Either elementId or fullElementId must be specified, not both.");
      }
    }
  }

  /**
   * Scope here means the analog of "scope" attribute in KeyboardHandlerHtmlTag. It must be prefixed by componentId when
   * the surrounding systemForm's "scope" is "screen".
   */
  protected String getTargetScope() {
    String scope = (String) this.pageContext.getAttribute(FormTag.FORM_FULL_ID_KEY, PageContext.REQUEST_SCOPE);
    if (!StringUtils.isBlank(this.subscope)) {
      if (StringUtils.isBlank(scope)) {
        scope = this.subscope;
      } else {
        scope = new StringBuffer(scope).append('.').append(this.subscope).toString();
      }
    }
    return scope;
  }

  /**
   * Given full element id (as it is in HTML) and event, creates a keyboard handler, that will invoke given event on the
   * element. The generated handler might look like this:
   * 
   * <pre>
   *  function() { return $('fullElementId').click(); };
   * </pre>
   * 
   * This string might be given as a parameter to a javascript function "uiRegisterKeypressHandler".
   * 
   * Any of the parameters may be null. In this case the componentId is searched for in the pageContext, and in other
   * cases taken as empty string.
   */
  public static final String createHandlerToInvokeJavascriptEvent(String fullElementId, String event) {
    return new StringBuffer("function(){return $('").append(fullElementId).append("').").append(event).append("()}")
        .toString();
  }

  /**
   * Given elementId relative the form inside which the tag is located, appends the relevant prefix to it to obtain
   * fullElementId.
   * 
   * @see #setElementId
   * @param elementId may not be null!
   */
  public static final String elementIdToFullElementId(PageContext pageContext, String elementId) {
    // Determine the full id.
    String scope = (String) pageContext.getAttribute(FormTag.FORM_FULL_ID_KEY, PageContext.REQUEST_SCOPE);
    if (!StringUtils.isBlank(scope)) {
      elementId = new StringBuffer(scope).append('.').append(elementId).toString();
    }
    return elementId;
  }
}
