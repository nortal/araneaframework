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
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.data.StringListData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class DemoComplexForm extends TemplateBaseWidget {
	private static final Logger log = Logger.getLogger(DemoComplexForm.class);
	
	/* Different controls and widgets we want to be accessible all the time. */
	private FormWidget complexForm;
	/* SelectControl - control which provides various selections from which one can be picked. */
	private SelectControl beastSelectionControl;
	/* MultiSelectControl - provides various selections from which zero, one or many can be picked */
	private MultiSelectControl concreteBeastMultiSelectionControl;

	protected void init() throws Exception {
		super.init();

		setViewSelector("demo/demoComplexForm");
		putViewData("formLabel", "Complex_Form");
		
		beastSelectionControl = new SelectControl();
		/* SelectControls can be added DisplayItems, one by one ... */
		beastSelectionControl.addItem(new DisplayItem("-choose-", "-choose-"));
		/* or collections of value objects, which must have  */
		beastSelectionControl.addDisplayItems(getSelectItems(), "sampleValue", "sampleDisplayString");

		beastSelectionControl.addOnChangeEventListener(new OnChangeEventListener() {
			public void onChange() throws Exception {
				/* Form must be converted before new values can be read from form.
				   As we want to be sure that entered data is valid (no random strings
				   where numbers are expected, length and content constraints are met)
				   we usually also validate data before using it for anything. */
				if (complexForm.convertAndValidate()) {
					// get the value from control
					String selectedBeast = (String)beastSelectionControl.getRawValue();
	
					// if no beast is selected, we remove the other elements from form 
					// that depend directly on selection being made - the controls
					// providing possibily for more specific beast selection.
					if (selectedBeast.equals("-choose-")) {
						complexForm.removeElement("concreteBeastControl");
						complexForm.removeElement("selectedBeastDesc");
						return;
					}
	
					// create the multiselectcontrol allowing selection of some beasts of selected type.
					concreteBeastMultiSelectionControl = new MultiSelectControl();
					for (Iterator i = getMultiSelectItems(selectedBeast).iterator(); i.hasNext(); ) {
						String current = (String) i.next();
						concreteBeastMultiSelectionControl.addItem(new DisplayItem(current, current));
					}
					
					// finally add both beast group description 
					complexForm.addElement("concreteBeastControl","#Choose " + selectedBeast, concreteBeastMultiSelectionControl, new StringListData(), false);
					complexForm.addElement("selectedBeastDesc", "#Description", new DisplayControl(), new StringData(), false);
					// if not dealing with beanforms, form element values are typically set this way
					complexForm.setValueByFullName("selectedBeastDesc", new SelectItem(selectedBeast).getDescription());
				}
			}
		});
		
		complexForm = new FormWidget();
		complexForm.addElement("beastSelection", "#Nature's Beasts", beastSelectionControl, new StringData(), true);
		
		addWidget("complexForm", complexForm);
	}
	
	// HELPER METHODS AND CLASSES
	private Collection getSelectItems() {
		List list = new ArrayList();
		// note that SelectItem is just an inner class and not anything special
		list.add(new SelectItem("  Bird  "));
		list.add(new SelectItem(" Animal "));
		list.add(new SelectItem("  Fish  "));
		list.add(new SelectItem(" Dragon "));
		return list;
	}
	
	private Collection getMultiSelectItems(String selectItem) {
		List result = new ArrayList();
		if (selectItem.equals("Bird")) {
			result.add("Chicken");
			result.add("Goose");
			result.add("Duck");
			result.add("Swan");
		} else if (selectItem.equals("Animal")) {
			result.add("Piglet");
			result.add("Pooh");
			result.add("Tiger");
			result.add("Cangaroo");
		} else if (selectItem.equals("Fish")) {
			result.add("Willy");
			result.add("Nemo");
			result.add("Dory");
			result.add("Marlin");
		} else if (selectItem.equals("Dragon")) {
			result.add("Smaug");
			result.add("Chrysophylax");
			result.add("Devon & Cornwall");
		} 

		return result;
	}

	public class SelectItem {
		public String sampleValue;
		public String sampleDisplayString;
		
		public SelectItem(String value) {
			this.sampleValue = value.trim();
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
		
		public String getDescription() {
			String result = "";
			if (sampleValue.equals("Bird")) {
				result = "Birds are bipedal, warm-blooded, oviparous vertebrates characterized primarily by feathers, forelimbs modified as wings, and hollow bones.";
			} else if (sampleValue.equals("Animal")) {
				result = "Animals are a major group of organisms, classified as the kingdom Animalia or Metazoa. In general they are multicellular, capable of locomotion and responsive to their environment, and feed by consuming other organisms. Their body plan becomes fixed as they develop, usually early on in their development as embryos, although some undergo a process of metamorphosis later on.";
			} else if (sampleValue.equals("Fish")) {
				result = "A fish is a poikilothermic (cold-blooded) water-dwelling vertebrate with gills.";
			} else if (sampleValue.equals("Dragon")) {
				result = "A dragon is a legendary creature, typically depicted as a large and powerful serpent or other reptile, with magical or spiritual qualities.";
			}
			return result;
		}
	}
}

