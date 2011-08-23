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
import org.araneaframework.core.util.Assert;
import org.araneaframework.jsp.tag.basic.BaseKeyboardHandlerTag;
import org.araneaframework.jsp.tag.basic.KeyboardHandlerHtmlTag;

/**
 * Invokes a "uiRegisterKeyboardHandler" javascript. This is basically the same stuff as
 * {@link org.araneaframework.jsp.tag.basic.KeyboardHandlerHtmlTag KeyboardHandlerHtmlTag} with a few modifications:
 * <ul>
 * <li>There is no <code>scope</code> attribute. Instead, the tag assumes that it is located inside a form, and takes
 * the full ID of that form as its scope.
 * <li>As an alternative to specifying the <code>handler</code> attribute, you may specify a form element and a
 * javascript event to invoke on that element. You specify the element by its ID relative to the surrounding form. The
 * event is given as a name of the javascript function to be invoked on the element. For example, if you specify the
 * element as "someButton", and event as "click", then when the required keyboard event occurs, the following javascript
 * will be executed:
 * <pre>
 * var el = $(&quot;&lt;form-full-id&gt;.someButton&quot;);
 * el.click();</pre>
 * </ul>
 * 
 * @see org.araneaframework.jsp.tag.basic.KeyboardHandlerHtmlTag
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 * 
 * @jsp.tag
 *  name = "formKeyboardHandler"
 *  body-content = "empty"
 *  description = "Registers an Aranea 'form' keyboard handler. Similar to keyboardHandler, but here the scope is relative to the form widget inside which the tag is located, and instead of specifying the handler, you may give the ID of an element, and a javascript event to call on that element."
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
   * Set the (relative) ID of the element on which the <code>event</code> should be invoked. Either you specify this or
   * the <code>fullElementId</code> parameter, but not both.
   * <p>
   * The two parameters are here because there are currently two "kinds" of <code>button</code> tags in Aranea Uilib.
   * One is a <code>&lt;button&gt;</code> tag that does not correspond to a form element. This tag has an "id"
   * parameter, and may look like this:
   * 
   * <pre>
   * &lt;button id=&quot;myButton&quot;/&gt;
   * </pre>
   * 
   * The other type of button corresponds to a form element and is rendered like this:
   * 
   * <pre>
   * &lt;button id=&quot;myForm.myButton&quot;/&gt;
   * </pre>
   * <p>
   * Therefore, if you would like to bind a keyboard handler to the first type of <code>button</code>, use the
   * <code>fullElementId</code> parameter. Otherwise use this <code>elementId</code> parameter and the tag will
   * automatically append the correct prefix.
   * 
   * @throws JspException Occurs when parameter is <code>null</code>.
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Set the (relative) ID of the element on which the <code>event</code> will be invoked. The ID is relative with respect to the surrounding form. Instead of this parameter, you may set element's full ID using the <code>fullElementId</code> parameter, but you can't set both attributes at once."
   */
  public void setElementId(String elementId) throws JspException {
    this.elementId = evaluateNotNull("elementId", elementId, String.class);
  }

  /**
   * @throws JspException Occurs when parameter is <code>null</code>.
   * @see #setElementId
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Set the full ID of the element on which the <code>event</code> will be invoked. The ID will be used as is. Instead of this parameter, you may set element's ID relative to the form using the <code>elementId</code> parameter, but you can't set both attributes at once."
   */
  public void setFullElementId(String fullElementId) throws JspException {
    this.fullElementId = evaluateNotNull("fullElementId", fullElementId, String.class);
  }

  /**
   * @throws JspException Occurs when parameter is <code>null</code>.
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Set the javascript event that should be invoked on the target element. "click" and "focus" are safe for most controls. If target element (the one given by <code>elementId</code> or <code>fullElementId</code>) is a select-box, you may also set 'select'. For more info, consult the javascript reference. This parameter is not foolproof and you may produce invalid javascript if you don't know what you are doing. 'onclick' is the default value to this parameter."
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

    validateParameters();

    if (this.handler == null) {
      if (this.elementId != null) { // If elementId was given, translate to fullElementId.
        this.fullElementId = elementIdToFullElementId(this.pageContext, this.elementId);
      }

      this.handler = createHandlerToInvokeJavascriptEvent(this.fullElementId, this.event);
    }

    // Write out.
    writeKeypressHandlerScript(out, getTargetScope(), this.handler);

    return SKIP_BODY;
  }

  protected void validateParameters() throws JspException {
    if (StringUtils.isBlank(this.handler)) {
      // One of elemenId/event must be specified
      if (this.event == null || this.elementId == null && this.fullElementId == null) {
        StringBuffer msg = new StringBuffer("You must specify JS handler or ((elementId | fullElementId) event) "
            + "parameters for FormKeyboardHandlerHtmlTag (currently elementId=");
        msg.append(this.elementId);
        msg.append(", fullElementId=");
        msg.append(this.fullElementId);
        msg.append(", event=");
        msg.append(this.event);
        msg.append(",subscope=");
        msg.append(this.subscope);
        msg.append(")!");

        throw new JspException(msg.toString());
      }

      // Only one of elementId/fullElementId must be specified
      if ((this.elementId == null) == (this.fullElementId == null)) {
        throw new JspException("Either elementId or fullElementId must be specified, not both.");
      }
    }
  }

  /**
   * Resolves the target scope of the event listener. Scope here means the analog of "scope" parameter in
   * KeyboardHandlerHtmlTag. When the tag is inside a form tag, the target scope will get its ID as prefix.
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
   * Given full element ID (as it is in HTML) and event, creates a keyboard handler, that will invoke given event on the
   * element. The generated handler might look like this:
   * 
   * <pre>
   *  function() { return $('fullElementId').click(); };
   * </pre>
   * 
   * This string might be given as a parameter to a JavaScript function "uiRegisterKeypressHandler".
   * 
   * Any of the parameters may be null. In this case the componentId is searched for in the pageContext, and in other
   * cases taken as empty string.
   */
  public static final String createHandlerToInvokeJavascriptEvent(String fullElementId, String event) {
    Assert.notNullParam(fullElementId, "fullElementId");
    Assert.notNullParam(event, "event");

    StringBuffer handler = new StringBuffer("Aranea.Keyboard.createElementEventHandler('");
    handler.append(fullElementId);
    handler.append("','");
    handler.append(event);
    handler.append("')");
    return handler.toString();
  }

  /**
   * Given elementId relative the form inside which the tag is located, appends the relevant prefix to it to obtain
   * fullElementId.
   * 
   * @see #setElementId
   * @param elementId May not be null!
   */
  public static final String elementIdToFullElementId(PageContext pageContext, String elementId) {
    Assert.notNullParam(pageContext, "pageContext");
    Assert.notNullParam(elementId, "elementId");

    String scope = (String) pageContext.getAttribute(FormTag.FORM_FULL_ID_KEY, PageContext.REQUEST_SCOPE);
    if (!StringUtils.isBlank(scope)) {
      elementId = new StringBuffer(scope).append('.').append(elementId).toString();
    }
    return elementId;
  }
}
