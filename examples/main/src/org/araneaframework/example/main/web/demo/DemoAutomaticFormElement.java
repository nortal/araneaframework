package org.araneaframework.example.main.web.demo;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.jsp.support.TagAttr;
import org.araneaframework.jsp.util.AutomaticFormElementUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * Demonstrates {@link FormElement} rendering with JSP &lt;ui:automaticFormElement&gt;
 * tag, which allows dynamically changing the tag with which {@link FormElement}
 * is rendered.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoAutomaticFormElement extends TemplateBaseWidget {
	private FormWidget form;
	private FormElement first;
	private boolean editable = false;

	protected void init() throws Exception {
		setViewSelector("demo/demoAutomaticFormElement");
		
		form = new FormWidget();
		first = form.addElement("first", "#First", new TextControl(), new StringData(), "InitialFirst", false);
		changeFormTags();
		
		addWidget("form", form);
	}

	public void handleEventReverse() {
		changeFormTags();
	}

	private void changeFormTags() {
		editable = !editable;
		if (!editable) {
			AutomaticFormElementUtil.setFormElementTag(first, "textInputDisplay", new TagAttr[] { new TagAttr("styleClass", "name") });
		}
		else {
			AutomaticFormElementUtil.setFormElementTag(first, "textInput", new TagAttr[] { new TagAttr("styleClass", "inpt") });
		}
	}
}
