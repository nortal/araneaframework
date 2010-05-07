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

package org.araneaframework.jsp.tag.basic;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.ContainedTagInterface;
import org.araneaframework.jsp.util.JspUtil;

/**
 * The base class for a set of "keyboard handler" tags. All these tags simply emit a "uiRegisterKeyboardHandler"
 * javascript invocation. Custom subclasses should basically use the "writeRegisterKeypressHandlerScript" static method
 * of this class to do their job.
 * 
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 */
public abstract class BaseKeyboardHandlerTag extends BaseTag implements ContainedTagInterface {

  protected Integer keyCode;

  protected String keyMetaCond;

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Integer key-code for which the event must be triggered. E.g. 13 means 'enter'. 'keyCode' and/or 'keyMetaCond' must be specified."
   */
  public void setKeyCode(String keyCode) throws JspException {
    this.keyCode = evaluateNotNull("keyCode", keyCode, Integer.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="A meta case-insensitive representation of key(s) press. The order of meta keys is not important. E.g. A, Ctrl+A, Shift+A, Alt+A, Ctrl+Shift+A. 'keyCode' and/or 'keyMetaCond' must be specified."
   * @since 2.0
   */
  public void setKeyMetaCond(String keyMetaCond) throws JspException {
    this.keyMetaCond = evaluateNotNull("keyMetaCond", keyMetaCond, String.class);
  }

  /**
   * Checks that either keyCode and/or key is specified. When overriding, don't forget to invoke super-implementation
   * first.
   */
  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    if (this.keyCode == null && StringUtils.isEmpty(this.keyMetaCond)) {
      throw new JspException("Either key or keyMetaCond must be specified for a keyboard handler tag.");
    }
    return SKIP_BODY;
  }

  protected void writeKeypressHandlerScript(Writer out, String scope, String handler) throws JspException, IOException {
    writeRegisterKeypressHandlerScript(out, scope, this.keyCode, this.keyMetaCond, handler);
  }

  /**
   * Writes "uiRegisterKeypressHandler" javascript, surrounded by &lt;script&gt tags. Throws exceptions if parameters
   * are not consistent (e.g. keyCode not specified).
   * 
   * @param out The response writer.
   * @param scope An optional scope of the event. For example, an element ID or Prototype expression ("div.target").
   * @param keyCode An optional key code the event would listen for.
   * @param keyMetaCond An optional keys expression (e.g. "ctrl+a").
   * @param handler The event handling function name (required).
   */
  public static final void writeRegisterKeypressHandlerScript(Writer out, String scope, Integer keyCode,
      String keyMetaCond, String handler) throws JspException, IOException {

    if (StringUtils.isBlank(handler)) {
      throw new JspException("'handler' may not be empty in the KeyboardHandlerHtmlTag");
    }

    JspUtil.writeJavaScriptStartTag(out);
    out.write("Aranea.Keyboard.registerKeypressHandler('");
    out.write(StringUtils.defaultIfEmpty(scope, ""));
    out.write("',");
    if (keyCode != null) {
      out.write(keyCode.toString());
    } else if (keyMetaCond != null) {
      out.write('\'');
      out.write(keyMetaCond);
      out.write('\'');
    } else {
      throw new JspException("KeyboardHandlerHtmlTag: keyCode or keyMetaCond must not be null!");
    }
    out.write(',');
    JspUtil.writeEscaped(out, handler);
    out.write(");");
    JspUtil.writeScriptEndTag(out);
  }
}
