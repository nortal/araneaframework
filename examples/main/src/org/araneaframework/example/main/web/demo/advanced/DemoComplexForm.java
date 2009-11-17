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

package org.araneaframework.example.main.web.demo.advanced;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.ListData;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * A form demo that shows interaction between select and multi-select boxes.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoComplexForm extends TemplateBaseWidget {

  private static final String FE_BEAST_DESC = "selectedBeastDesc";

  private static final String FE_BEAST_MULTI_SELECT = "concreteBeastControl";

  private static final String MSG_CHANGED = "complexForm.multiSelect.changed";

  /*
   * Different controls and widgets we want to be accessible all the time are made instance variables by convention.
   */
  private FormWidget complexForm;

  /*
   * SelectControl - control which provides various selections from which one must be picked.
   */
  private SelectControl<SelectItem> beastSelectionControl;

  @Override
  protected void init() throws Exception {
    setViewSelector("demo/demoComplexForm");
    putViewData("formLabel", "complexForm.ajax");

    // Note that the constructor takes the value property and the label property of the target class:
    this.beastSelectionControl = new SelectControl<SelectItem>(SelectItem.class, "label", "value");

    // Items can be added to SelectControls one by one or all-together:
    this.beastSelectionControl.addItem("select.choose", null);
    this.beastSelectionControl.addItems(getSelectItems());

    /* Adds the onChange event listener to selectControl */
    this.beastSelectionControl.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() throws Exception {

        // Form must be converted before new values can be read from form. As we want to be sure that entered data is
        // valid (no random strings where numbers are expected, length and content constraints are met) we usually also
        // validate data before using it for anything.

        if (DemoComplexForm.this.complexForm.convertAndValidate()) {
   
          // Get the value from control (what beast was selected).

          SelectItem selectedBeast = DemoComplexForm.this.beastSelectionControl.getRawValue();

          // If no beast is selected in our select control, we remove the other elements from form that depend directly
          // on selection being made - the controls providing possibility for more specific beast selection.

          if (selectedBeast == null) {
            DemoComplexForm.this.complexForm.removeElement(FE_BEAST_MULTI_SELECT);
            DemoComplexForm.this.complexForm.removeElement(FE_BEAST_DESC);

          } else {
            // Show a message indicating which beast was selected:
            DemoComplexForm.this.getMessageCtx().showInfoMessage(MSG_CHANGED, t(selectedBeast.label));

            // Continue with adding multi-select controls (the concrete representatives of the species):
            addBeastMultiSelect(DemoComplexForm.this.complexForm, selectedBeast);
          }
        }
      }
    });

    this.complexForm = new FormWidget();
    this.complexForm.addElement("beastSelection", "complexForm.chooseBeast", this.beastSelectionControl,
        new Data<SelectItem>(SelectItem.class)); // You might want to create custom (shorter) class for SelectItem Data.

    addWidget("complexForm", this.complexForm);
  }

  /**
   * Adds a more specific multi-select depending on the selected beast. Also adds description (display) element with
   * information about the selected beast.
   */
  private void addBeastMultiSelect(FormWidget form, SelectItem selectedBeast) {

    // Create the MultiSelectControl allowing selection of some more specific beasts of selected type:
    MultiSelectControl<SelectItem> beastMultiSelect = new MultiSelectControl<SelectItem>(SelectItem.class, "label", "value");

    for (String label : getMultiSelectItems(selectedBeast)) {
      beastMultiSelect.addItem(new SelectItem(label));
    }

    // Finally add both beast group description and beast selection control to this widget:

    String label = "#" + t("common.choose") + " " + t(selectedBeast.label);
    form.addElement(FE_BEAST_MULTI_SELECT, label, beastMultiSelect, new ListData<SelectItem>(SelectItem.class));

    // Creating the element for showing the description of the selected beast. The value (description) is the last
    // parameter.
    form.addElement(FE_BEAST_DESC, "common.description", new DisplayControl(), new StringData(),
        selectedBeast.getDescription());
  }

  /**
   * Returns the main select data items. Note that SelectItem is just an inner class of this class and not anything
   * special.
   * 
   * @return A list of select items to show.
   */
  private List<SelectItem> getSelectItems() {
    List<SelectItem> list = new ArrayList<SelectItem>();
    list.add(new SelectItem(BIRD));
    list.add(new SelectItem(ANIMAL));
    list.add(new SelectItem(FISH));
    list.add(new SelectItem(DRAGON));
    return list;
  }

  /**
   * Return the data for the more specific multi-select depending on the value that was selected in the main select
   * control.
   * 
   * @param selectItem The value that was selected in the main select.
   * @return A list of String containing the label keys to resolve the label (and value) of the multi-select items.
   */
  private String[] getMultiSelectItems(SelectItem selectItem) {
    String value = selectItem == null ? null : selectItem.value;
    String[] result = new String[0];

    if (getValue(BIRD).equals(value)) {
      result = DemoComplexForm.BIRDS;

    } else if (getValue(ANIMAL).equals(value)) {
      result = DemoComplexForm.ANIMALS;

    } else if (getValue(FISH).equals(value)) {
      result = DemoComplexForm.FISHES;

    } else if (getValue(DRAGON).equals(value)) {
      result = DemoComplexForm.DRAGONS;
    }

    return result;
  }

  // Utility method for this demo. Returns a sample value based on the label ID.
  private static String getValue(String label) {
    return StringUtils.substringAfterLast(label, ".");
  }

  // ==========================================================================================
  //  The data to show is defined here. The constants contain the keys to resolve labels,
  //  while the last part of the key following the last dot is used as the corresponding value
  // ==========================================================================================

  private static final String BIRD = "complexForm.value.bird";

  private static final String ANIMAL = "complexForm.value.animal";

  private static final String FISH = "complexForm.value.fish";

  private static final String DRAGON = "complexForm.value.dragon";

  private static final String[] BIRDS = { BIRD + ".chicken", BIRD + ".goose", BIRD + ".duck", BIRD + ".swan" };

  private static final String[] ANIMALS = { ANIMAL + ".piglet", ANIMAL + ".pooh", ANIMAL + ".tiger",
      ANIMAL + ".cangaroo" };

  private static final String[] FISHES = { FISH + ".willy", FISH + ".nemo", FISH + ".dory", FISH + ".marlin" };

  private static final String[] DRAGONS = { DRAGON + ".smaug", DRAGON + ".chrysophylax", DRAGON + ".devon" };

  // =============================================================================================
  //  Here is the select item model used in this demo. Also note that Aranea comes by default
  //  with two similar classes: DisplayItem and BeanDisplayItem. However, we have some additional
  //  custom logic defined here as regards to interpreting the value of this item.
  // =============================================================================================

  public static class SelectItem implements Serializable {

    private String value;

    private String label;

    public SelectItem() {}

    public SelectItem(String label) {
      this.label = label;
      this.value = DemoComplexForm.getValue(label);
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public String getLabel() {
      return this.label;
    }

    public String getValue() {
      return this.value;
    }

    public String getDescription() {
      String result = "";
      if (BIRD.equals(this.label)) {
        result = "complexForm.desc.bird";
      } else if (ANIMAL.equals(this.label)) {
        result = "complexForm.desc.animal";
      } else if (FISH.equals(this.label)) {
        result = "complexForm.desc.fish";
      } else if (DRAGON.equals(this.label)) {
        result = "complexForm.desc.dragon";
      }
      return result;
    }
  }
}
