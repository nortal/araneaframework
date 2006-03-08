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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class DemoComplexForm extends TemplateBaseWidget {
	private FormWidget complexForm;
	private SelectControl eventTypeControl;

	protected void init() throws Exception {
		super.init();
		setViewSelector("demo/demoComplexForm");
		
		putViewData("formLabel", "Complex_Form");
		
		eventTypeControl = new SelectControl();
		eventTypeControl.addDisplayItems(getMultiSelectItems(), "sampleValue", "sampleDisplayString");
		
		complexForm = new FormWidget();
		complexForm.addElement("multiSelect", "#TheLabel", eventTypeControl, new StringData(), true);
		
		addWidget("complexForm", complexForm);
	}
	
	private Collection getMultiSelectItems() {
		List list = new ArrayList();
		list.add(new MultiSelectItem("First choice"));
		list.add(new MultiSelectItem("Second choice"));
		list.add(new MultiSelectItem("Third choice"));
		list.add(new MultiSelectItem("Fourth choice"));
		return list;
	}
	
	public class MultiSelectItem {
		public String sampleValue;
		public String sampleDisplayString;
		
		public MultiSelectItem(String value) {
			this.sampleValue = value;
			this.sampleDisplayString = "> " + value + " <";
		}

		public String getSampleDisplayString() {
			return sampleDisplayString;
		}

		public void setSampleDisplayString(String sampleDisplayString) {
			this.sampleDisplayString = sampleDisplayString;
		}

		public String getSampleValue() {
			return sampleValue;
		}

		public void setSampleValue(String sampleValue) {
			this.sampleValue = sampleValue;
		}
	}
}

