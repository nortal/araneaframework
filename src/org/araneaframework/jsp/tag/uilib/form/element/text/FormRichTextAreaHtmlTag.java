package org.araneaframework.jsp.tag.uilib.form.element.text;

import javax.servlet.jsp.JspException;

/**
 * Standard text input form element tag with rich text editor capabilities.
 * The tag uses a special style class denoted by EDITOR_SELECTOR which makes
 * it possible for the RTE library to locate the area and wrap it. 
 * 
 * @author Toomas Römer
 * 
 * @jsp.tag
 *   name = "richtextarea"
 *   body-content = "JSP"
 *   description = "Form text input field (textarea) wrapped with rich text editor capabilities."
 */
public class FormRichTextAreaHtmlTag extends FormTextareaHtmlTag{
	public static final String EDITOR_SELECTOR = "richTextEditor";
	
	protected String getStyleClass() throws JspException  {
		return EDITOR_SELECTOR;
	}
}
