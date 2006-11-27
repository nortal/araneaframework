package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.uilib.form.control.EhlFancyAutoCompleteControl;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl.ViewModel;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @jsp.tag 
 *   name = "ehlFancyAutoCompleteTextInput" 
 * 	 body-content = "JSP" 
 *   description = "Form text input field, represents UiLib &quot;AutoCompleteTextControl&quot;."
 */
public class EhlFancyAutoCompleteTextInputHtmlTag extends FormAutoCompleteTextInputHtmlTag {
	protected String constructACRegistrationScript(ViewModel viewModel, StringBuffer acRequestUrl) {
		EhlFancyAutoCompleteControl.ViewModel model = (EhlFancyAutoCompleteControl.ViewModel) viewModel;
		
		if (model.getDisplayItems() == null)
			return super.constructACRegistrationScript(viewModel, acRequestUrl);

		StringBuffer script = new StringBuffer();
	    script.append("new Autocompleter.Local(\"");
	    script.append(getScopedFullFieldId());
	    script.append("\", \"ACdiv.");
	    script.append(getScopedFullFieldId());
	    script.append("\", new Array(");
	    
	    for (Iterator i = model.getDisplayItems().iterator(); i.hasNext(); ) {
	    	DisplayItem item = (DisplayItem) i.next();
	    	script.append("'");
	    	script.append(item.getDisplayString());
	    	script.append("'");
	    	if (i.hasNext())
	    		script.append(',');
	    }
	    
	    script.append("), {");
	    
	    // Autocompleter options
	    for (Iterator i = getOptionMap().entrySet().iterator(); i.hasNext(); ) {
	    	Map.Entry entry = (Map.Entry) i.next();
	    	script.append((String)entry.getKey());
	    	script.append(":");
	    	script.append((String)entry.getValue());
	    	if (i.hasNext()) script.append(",");
	    }

	    script.append("});");

		return script.toString();
	}
	
	protected void assertControlType(String type) throws JspException {
		super.assertControlType("EhlFancyAutoCompleteControl");
	}
}
