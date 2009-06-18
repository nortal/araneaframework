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

package org.araneaframework.jsp.tag.basic;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.ContainedTagInterface;
import org.araneaframework.jsp.util.JspUtil;

/**
 * The base class for a set of "keyboard handler" tags.
 * All these tags simply emit a "uiRegisterKeyboardHandler" javascript invocation.
 * Custom subclasses should basically use the "writeRegisterKeypressHandlerScript" static method of
 * this class to do their job.
 * 
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 */
public abstract class BaseKeyboardHandlerTag extends BaseTag implements ContainedTagInterface {
  protected String defaultKeyCode;
  protected String intKeyCode;
  protected String keyCode;
  
  protected String defaultKey;
  protected String intKey;
  protected String key;
  
  protected String keyCombo;
  
	//
	// Attributes
	//
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Keycode to which the event must be triggered. 13 means enter.
          Either keyCode or key must be specified, but not both." 
	 */
	public void setKeyCode(String keyCode) throws JspException {
		this.keyCode = (String) evaluateNotNull("keyCode", keyCode, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Keycode to which the event must be triggered. 13 means enter.
          Either keyCode or key must be specified, but not both." 
	 */
	public void setKeyCombo(String keyCombo) throws JspException {
		this.keyCombo = (String) evaluateNotNull("keyCombo", keyCombo, String.class);
	}
	
	/**
	 * Key to which the event must be triggered.
	 * Similar to {@link #setKeyCode setKeyCode}, but accepts key "aliases" instead of codes.
	 * For example, to react to the return key, you either specify keyCode = "13",
	 * or key = "enter" (but you can't specify both!).
	 * A list of available aliases is given in {@link #keyToKeyCode}.
	 * @see #keyToKeyCode
	 * 
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Key, to which the event must be triggered. Key is specified as a certain 'alias'. The alias may be an
          ASCII character or a digit (this will denote the corresponding key on a US keyboard), a space (' '), or
          one of the following: 'return', 'escape', 'backspace', 'tab', 'shift', 'control', 'space', 'f1',
          'f2', ..., 'f12'."
	 */
	public void setKey(String key) throws JspException {
		this.key = (String) evaluateNotNull("key", key, String.class);
	}
  
  //
  // Implementation
  //
		
  /**
   * Checks that either keyCode or key is specified (not both), and initializes the keyCode field.
   * When overriding don't forget to invoke superimplementation first.
   */
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		intKey = (key == null && keyCode == null) ? defaultKey : key;
		intKeyCode = (key == null && keyCode == null) ? defaultKeyCode : keyCode;
		
		if (!(intKeyCode == null ^ intKey == null) && !(intKey == null ^ keyCombo == null)) 
			throw new JspException("Either key or keyCode or keyCombo must be specified for a keyboard handler tag.");
		
		if (intKeyCode == null && keyCombo == null) {
			int iKeyCode = keyToKeyCode(intKey);
			if (iKeyCode == 0) throw new JspException("Invalid key alias specified (" + key + ")");
			intKeyCode = String.valueOf(iKeyCode);
		}
		
		return SKIP_BODY;
	}
  
   /**
   * Writes "uiRegisterKeypressHandler" javascript, surrounded by &lt;script&gt tags.
   * Throws exceptions if parameters are not consistent (e.g. keyCode not specified).
   */
  public static final void writeRegisterKeypressHandlerScript(Writer out, String scope, String keyCode, String handler) throws JspException, IOException {
	if (StringUtils.isBlank(handler)) throw new JspException("handler may not be empty in the KeyboardHandlerHtmlTag");
	if (StringUtils.isBlank(scope)) scope = "";
	
	JspUtil.writeStartTag_SS(out, "script type='text/javascript'");
	out.write("Aranea.KB.registerKeypressHandler('");
	out.write(scope);
	out.write("', ");
	out.write(keyCode);
	out.write(", ");
	JspUtil.writeEscaped(out, handler);
	out.write(");");
	JspUtil.writeEndTag_SS(out, "script");
  }
  
  public static final void writeRegisterKeycomboHandlerScript(Writer out, String scope, String keyCombo, String handler) throws JspException, IOException {
	  if (StringUtils.isBlank(handler)) throw new JspException("handler may not be empty in the KeyboardHandlerHtmlTag");
	  if (StringUtils.isBlank(scope)) scope = "";

	  JspUtil.writeStartTag_SS(out, "script type='text/javascript'");
	  out.write("Aranea.KB.registerKeyComboHandler('");
	  out.write(scope);
	  out.write("', '");
	  out.write(keyCombo);
	  out.write("', ");
	  JspUtil.writeEscaped(out, handler);
	  out.write(");");
	  JspUtil.writeEndTag_SS(out, "script");
  }

  /**
   * Translates a "key alias" to its key-code.
   * Under key-code we mean the keyboard code reported by the javascript
   * keyboard event, when the corresponding key is pushed on a usual US keyboard.
   * <br><br>
   * For example keyToKeyCode("A") returns 65.
   * keyToKeyCode("RETURN") returns 13.
   * Available key aliases are:
   * <ul>
   * <li>All ASCII characters and digits</li>
   * <li>"F1"..."F12"</li>
   * <li>"RETURN"/"ENTER", "BACKSPACE", "ESCAPE", "TAB", "SHIFT", "CONTROL", "SPACE"</li>
   * <li>" " (space) is space</li> 
   * </ul>
   * Case does not matter.
   * If input is invalid, zero iz returned.
   * @param key
   * @return corresponding keyCode (that is supposed 
   */
  public static final int keyToKeyCode(String key) {
  	if (key == null) return 0;
  	if (key.length() == 1) {
  		char c = key.toUpperCase().charAt(0);
  		if ((c >= 'A' && c <= 'Z') || 
  		    (c >= '0' && c <= '9') ||
			(c == ' ')) return c;
  		else return 0;
  	}
  	else {
  		Integer code = (Integer)keyToKeyCodeMap.get(key.toLowerCase());
  		if (code != null) return code.intValue();
  		else return 0; 
  	}
  }
  
  // This map is used in "keyToKeyCode"
  private static final Map keyToKeyCodeMap; 
  static {
  	keyToKeyCodeMap = new HashMap();
  	keyToKeyCodeMap.put("return", new Integer(13));
  	keyToKeyCodeMap.put("enter", new Integer(13));
  	keyToKeyCodeMap.put("escape", new Integer(27));
  	keyToKeyCodeMap.put("backspace", new Integer(8));
  	keyToKeyCodeMap.put("tab", new Integer(9));
  	keyToKeyCodeMap.put("shift", new Integer(16));
  	keyToKeyCodeMap.put("control", new Integer(17));
  	keyToKeyCodeMap.put("space", new Integer(32));
  	// Add function keys
  	for (int i = 1; i <= 12; i++) {
  		keyToKeyCodeMap.put("f" + i, new Integer(111 + i));
  	}
  }
}
