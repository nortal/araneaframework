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

package org.araneaframework.example.main.web.demo;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * Sample widget for using the rich textarea tag. The widget
 * shows an area with richtext controls. When users submits
 * the area's contents it is displayed in a preview div.
 * 
 * See accompanying 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class DemoRichTextForm extends TemplateBaseWidget {
	private FormWidget form;
	private StringData areaContents;
	
	protected void init() throws Exception {
		super.init();
		
		setViewSelector("demo/demoRichTextArea");
		
		form = new FormWidget();	
		areaContents = new StringData();
		areaContents.setValue(defaultContents);
		FormElement el = form.createElement("#Element", new TextareaControl(), 
												areaContents, false);
		form.addElement("richArea", el);
		
		ButtonControl button = new ButtonControl();
		button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testForm"));
		form.addElement("button", "#Preview", button, null, false);
		
		addWidget("form", form);
	}
	
	public void handleEventTestForm() throws Exception {
		form.convert();
		putViewData("preview", areaContents.getValue());
	}
	
	private static final String defaultContents = "<div class='post' id='post-12'>"+
                                "<h2><a href='http://blog.araneaframework.org/2006/05/16/aranea-web-framework-10-m3-released/' rel='bookmark' title='Permanent Link to Aranea Web Framework 1.0 M3 Released'>Aranea Web Framework 1.0 M3 Released</a></h2>"+
                                "<small>May 16th, 2006 <!-- by ekabanov --></small>"+
                                "<div class='entry'>"+
                                "<p><em>This release provides improvements in configuration, JSP tag API, AJAX and popups. Since the release was delayed due to technical problems it also contains early support for holding session state on client and lightweight JSP container.</em></p>"+
                            	"<p>The <a href='http://araneaframework.org'>Aranea</a> 1.0 M3 release is a major milestone on the way to the final release. It contains both functional and API improvements, and numerous bugfixes. The major improvements are as follows:</p>"+
                        		"<ul><li><strong>JSP tags.</strong> We want it to make it very simple to create new Aranea JSP tags and customize existing ones. For that we have refactored the current tag library to be closer to the standard API and now provide a simple set of utility classes instead of scattered functions.</li>"+
                        		"<li><strong>Configuration. </strong>Aranea now supports the <em>Configuration by convention</em> by providing a default, overridable configuration. It is now possible to configure simple Aranea setups using only web.xml,  yet be able to override (or add your own) filters when needed.</li>"+
                        		"<li><strong>Popups. </strong>Popup API has been significantly improved and now it should be very easy to open and use arbitrary popups windows.</li>"+
                    			"<li><strong>Partial page update. </strong>Partial page update allows to update only parts of the HTML page by sending a background AJAX request. Its performance has been significantly improved in this milestone.</li>"+
                				"<li><strong>Client state serialization. </strong>Aranea allows now to serialize the session state to client (optionally compressing it) to save server memory. This functionality is in prototype stage at the moment and will be finalized in 1.0 M4.</li>"+
                				"<li><strong>Lightweight JSP container. </strong>Aranea uses an internal implementation of parts of the JSP spec to power the live examples in the distribution. This feature (which will become an Aranea subproject) is also in its prototype phase and more information will follow later.</li>"+
                				"</ul><p>The next release is 1.0 M4, which is scheduled for June and should be a last, feature complete milestone before the final release, which will follow right after.</p>"+
                				"<p><em><a href='http://araneaframework.org/'>Aranea</a> is an Open Source (APL 2.0) Java Hierarchical Model-View-Controller Web Framework that provides a common simple approach to building the web application components, reusing custom or general GUI logic and extending the framework. The framework enforces programming using Object-Oriented techniques with POJOs and provides a JSP tag library that facilitates programming web GUIs without writing HTML.</em>"+
                				"</p></div>"; 
}
