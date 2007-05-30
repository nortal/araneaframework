package org.araneaframework.example.common.tags.select;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.example.select.StraightTextControl;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard text input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "textInput"
 *   body-content = "JSP"
 *   description = "Form text input field, represents UiLib "TextControl"."
 */
public class StraightTextInputHtmlTag extends BaseFormElementHtmlTag {
	  protected Long size;
	  protected String onChangePrecondition;

	  {
	    baseStyleClass = "aranea-text";
	  }
	  
	  protected int doStartTag(Writer out) throws Exception {
	    int result = super.doStartTag(out);
	    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
	    return result;
	  }  

	  /* ***********************************************************************************
	   * Tag attributes
	   * ***********************************************************************************/
	  /**
	   * @jsp.attribute
	   *   type = "java.lang.String"
	   *   required = "false"
	   *   description = "Horizontal size, in characters." 
	   */
	  public void setSize(String size) throws JspException {
	    this.size = (Long)evaluate("size", size, Long.class);
	  }

	  /**
	   * @jsp.attribute
	   *   type = "java.lang.String"
	   *   required = "false"
	   *   description = "Precondition for deciding whether go to server side or not." 
	   */
	  public void setOnChangePrecondition(String onChangePrecondition) throws JspException {
	    this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
	  }

	  /* ***********************************************************************************
	   * INPUT writing functions
	   * ***********************************************************************************/

	  protected void writeTextInput(Writer out, String inputType) throws Exception {
	    writeTextInput(out, inputType, true, new HashMap());
	  }

	  protected void writeTextInput(Writer out, String inputType, boolean writeValue, Map customAttributes) throws Exception {
	    String name = this.getFullFieldId();
	    StraightTextControl.ViewModel viewModel = ((StraightTextControl.ViewModel)controlViewModel);

	    // Write
	    JspUtil.writeOpenStartTag(out, "input");
	    JspUtil.writeAttribute(out, "id", name);
	    JspUtil.writeAttribute(out, "name", name);    
	    JspUtil.writeAttribute(out, "class", getStyleClass());
	    JspUtil.writeAttribute(out, "style", getStyle());
	    JspUtil.writeAttribute(out, "type", inputType);
	    if (writeValue)
	      JspUtil.writeAttribute(out, "value", viewModel.getSimpleValue());
	    JspUtil.writeAttribute(out, "size", size);
	    JspUtil.writeAttribute(out, "tabindex", tabindex);

	    for (Iterator i = customAttributes.entrySet().iterator(); i.hasNext(); ) {
	      Map.Entry attribute = (Map.Entry) i.next();
	      
	      JspUtil.writeAttribute(out, "" + attribute.getKey(), attribute.getValue());
	    }

	    if (viewModel.isDisabled())
	      JspUtil.writeAttribute(out, "disabled", "true");
	    //XXX: pjdppodjo
	    if (events && false) {
	      // We use "onblur" to simulate the textbox's "onchange" event
	      // this is _not_ good, but there seems to be no other way
	      JspUtil.writeAttribute(out, "onfocus", "saveValue(this)");
	      if (onChangePrecondition == null)
	    	  onChangePrecondition = "return isChanged('" + name + "');";
	      this.writeSubmitScriptForUiEvent(out, "onblur", derivedId, "onChanged", onChangePrecondition, updateRegionNames);
	    }
	    JspUtil.writeAttributes(out, attributes);
	    JspUtil.writeCloseStartEndTag_SS(out);
	  }

	  public void doFinally() {
	    super.doFinally();
	    onChangePrecondition = null;
	  }
	
	
	  protected int doEndTag(Writer out) throws Exception {
		    // Type check
		    assertControlType("StraightTextControl");

		    StraightTextControl.ViewModel viewModel = ((StraightTextControl.ViewModel)controlViewModel);

		    // Write
		    Map attributes = new HashMap();
		    attributes.put("maxLength", viewModel.getMaxLength());
		    writeTextInput(out, "text", true, attributes);

		    // Continue
		    super.doEndTag(out);
		    return EVAL_PAGE;
		  }
}


