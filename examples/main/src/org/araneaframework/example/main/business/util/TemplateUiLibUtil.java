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

package org.araneaframework.example.main.business.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.araneaframework.jsp.support.FormElementViewSelector;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.reader.BeanFormReader;
import org.araneaframework.uilib.form.reader.BeanFormWriter;


/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */

public class TemplateUiLibUtil {
	
		private static final Map CONTROLS_TO_EDITABLE_TAGS = new HashMap();
		private static final Map CONTROLS_TO_DISPLAY_TAGS = new HashMap();

	  public static final String RANGE_START = "start";
	  public static final String RANGE_END = "end";
	  
	  
		static {
			CONTROLS_TO_EDITABLE_TAGS.put(ButtonControl.class, "button");

			CONTROLS_TO_EDITABLE_TAGS.put(CheckboxControl.class, "checkbox");

			CONTROLS_TO_EDITABLE_TAGS.put(FileUploadControl.class, "fileUpload");

			CONTROLS_TO_EDITABLE_TAGS.put(MultiSelectControl.class, "multiSelect");
			CONTROLS_TO_EDITABLE_TAGS.put(SelectControl.class, "select");

			CONTROLS_TO_EDITABLE_TAGS.put(TextareaControl.class, "textarea");
			CONTROLS_TO_EDITABLE_TAGS.put(TextControl.class, "textInput");
			CONTROLS_TO_EDITABLE_TAGS.put(NumberControl.class, "numberInput");
			CONTROLS_TO_EDITABLE_TAGS.put(FloatControl.class, "floatInput");

			CONTROLS_TO_EDITABLE_TAGS.put(DateControl.class, "dateInput");
			CONTROLS_TO_EDITABLE_TAGS.put(DateTimeControl.class, "dateTimeInput");
			CONTROLS_TO_EDITABLE_TAGS.put(TimeControl.class, "timeInput");

			//-------------------------------------------------------------
			CONTROLS_TO_DISPLAY_TAGS.put(CheckboxControl.class, "checkboxDisplay");

			CONTROLS_TO_DISPLAY_TAGS.put(MultiSelectControl.class, "multiSelectDisplay");
			CONTROLS_TO_DISPLAY_TAGS.put(SelectControl.class, "selectDisplay");

			CONTROLS_TO_DISPLAY_TAGS.put(TextareaControl.class, "textareaDisplay");
			CONTROLS_TO_DISPLAY_TAGS.put(TextControl.class, "textInputDisplay");
			CONTROLS_TO_DISPLAY_TAGS.put(NumberControl.class, "numberInputDisplay");
			CONTROLS_TO_DISPLAY_TAGS.put(FloatControl.class, "floatInputDisplay");

			CONTROLS_TO_DISPLAY_TAGS.put(DateControl.class, "dateInputDisplay");
			CONTROLS_TO_DISPLAY_TAGS.put(DateTimeControl.class, "dateTimeInputDisplay");
			CONTROLS_TO_DISPLAY_TAGS.put(TimeControl.class, "timeInputDisplay");
		}

		private TemplateUiLibUtil() {}

		/**
		 * Fills the DTO with data read from the form.
		 */
		public static Object readDtoFromForm(Object dto, FormWidget form) {
			if (dto == null)
				throw new NullPointerException("The DTO that is read from the form mustn't be null!");

			BeanFormReader voReader = new BeanFormReader(form);
			voReader.readFormBean(dto);

			return dto;
		}

		/**
		 * Fills the form with DTO data.
		 */
		public static void writeDtoToForm(Object dto, FormWidget form) {
			if (dto == null)
				throw new NullPointerException("The DTO that is written to the form mustn't be null!");

			BeanFormWriter writer = new BeanFormWriter(dto.getClass());
			writer.writeFormBean(form, dto);
		}

		/**
		 * Assigns a view selector to the specified element.
		 *
		 * @param form parent form or composite element.
		 * @param simpleElementId id of the simple element.
		 * @param viewSelector view selector to set to the element.
		 */
		public static void setFormElementViewSelector(FormWidget form, String simpleElementId,
		                                              FormElementViewSelector viewSelector) {
			form.getElementByFullName(simpleElementId).setProperty(FormElementViewSelector.FORM_ELEMENT_VIEW_SELECTOR_PROPERTY,
			                                                             viewSelector);
		}

		/**
		 * Assigns a tag to the specified element.
		 *
		 * @param form parent form or composite element.
		 * @param simpleElementId id of the simple element.
		 * @param tag name of the tag that will be used to render the element.
		 */
		public static void setFormElementTag(FormWidget form, String simpleElementId, String tag) {
			setFormElementTag(form, simpleElementId, tag, new HashMap());
		}

		/**
		 * Assigns a tag to the specified element.
		 *
		 * @param form parent form or composite element.
		 * @param simpleElementId id of the simple element.
		 * @param tag name of the tag that will be used to render the element.
		 * @param attributePairs tag custom attributes.
		 */
		public static void setFormElementTag(FormWidget form, String simpleElementId, String tag,
		                                     TagAttr[] attributePairs) {
			Map attributes = new HashMap();

			for (int i = 0; i < attributePairs.length; i++)
				attributes.put(attributePairs[i].getName(), attributePairs[i].getValue());

			setFormElementTag(form, simpleElementId, tag, attributes);
		}

		/**
		 * Assigns a tag to the specified element.
		 *
		 * @param form parent form or composite element.
		 * @param simpleElementId id of the simple element.
		 * @param tag name of the tag that will be used to render the element.
		 * @param attributes tag custom attributes.
		 */
		public static void setFormElementTag(FormWidget form, String simpleElementId, String tag, Map attributes) {
			setFormElementViewSelector(form, simpleElementId, new FormElementViewSelector(tag, attributes));
		}

		/**
		 * Assigns the default editable (aka input) tags to all of the elements of the form.
		 * @param form parent form or composite element.
		 */
		public static void setFormElementDefaultEditableTags(FormWidget form) {
			for (Iterator i = form.getElements().entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				
				String elementId = (String) entry.getKey();
				GenericFormElement element = (GenericFormElement) entry.getValue();

				if (element instanceof FormWidget)
					setFormElementDefaultEditableTags((FormWidget) element);
				else if (element instanceof FormElement) {
					FormElement simpleElement = (FormElement) element;
					setFormElementTag(form, elementId,
					                  (String) CONTROLS_TO_EDITABLE_TAGS.get(simpleElement.getControl().getClass()));
				}
			}
		}

		/**
		 * Assigns the default display (aka read-only) tags to all of the elements of the form.
		 * @param form parent form or composite element.
		 */
		public static void setFormElementDefaultDisplayTags(FormWidget form) {
			for (Iterator i = form.getElements().entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				
				String elementId = (String) entry.getKey();
				GenericFormElement element = (GenericFormElement) entry.getValue();

				if (element instanceof FormWidget)
					setFormElementDefaultDisplayTags((FormWidget) element);
				else if (element instanceof FormElement) {
					FormElement simpleElement = (FormElement) element;
					setFormElementTag(form, elementId,
					                  (String) CONTROLS_TO_DISPLAY_TAGS.get(simpleElement.getControl().getClass()));
				}
			}
		}
}
