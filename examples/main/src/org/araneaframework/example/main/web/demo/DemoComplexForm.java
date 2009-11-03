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

package org.araneaframework.example.main.web.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.data.StringListData;

/**
 * A form demo that shows interaction between select and multi-select boxes.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoComplexForm extends TemplateBaseWidget {

  private static final String FE_BEAST_DESC = "selectedBeastDesc";

  private static final String FE_BEAST_MULTI_SELECT = "concreteBeastControl";

  private static final String MSG_CHANGED = "demo.complexForm.multiSelect.changed";

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
    putViewData("formLabel", "demo.complexForm.ajax");

    // Note that the constructor takes the value property and the label property of the target class:
    this.beastSelectionControl = new SelectControl<SelectItem>(SelectItem.class, "value", "label");

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
            return;
          }

          DemoComplexForm.this.getMessageCtx().showInfoMessage(MSG_CHANGED, selectedBeast.label);

          // Continue with adding multi-select controls:
          addBeastMultiSelect(DemoComplexForm.this.complexForm, selectedBeast);
        }
      }
    });

    this.complexForm = new FormWidget();
    this.complexForm.addElement("beastSelection", "natures.beasts", this.beastSelectionControl, new StringData());

    addWidget("complexForm", this.complexForm);
  }

  /**
   * Adds a more specific multi-select depending on the selected beast. Also adds description (display) element with
   * information about the selected beast.
   */
  private void addBeastMultiSelect(FormWidget form, SelectItem selectedBeast) {

    // Create the MultiSelectControl allowing selection of some more specific beasts of selected type:
    MultiSelectControl<SelectItem> beastMultiSelect = new MultiSelectControl<SelectItem>("label", "value");

    for (String label : getMultiSelectItems(selectedBeast)) {
      beastMultiSelect.addItem(new SelectItem(label));
    }

    // Finally add both beast group description and beast selection control to this widget:
    form.addElement(FE_BEAST_MULTI_SELECT, "#" + t("common.Choose") + " " + t(selectedBeast.label), beastMultiSelect,
        new StringListData());

    form.addElement(FE_BEAST_DESC, "common.Description", new DisplayControl(), new StringData());

    // If not dealing with bean forms, form element values are typically set this way
    DemoComplexForm.this.complexForm.setValueByFullName(FE_BEAST_DESC, selectedBeast.getDescription());
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
  private Collection<String> getMultiSelectItems(SelectItem selectItem) {
    String value = selectItem == null ? null : selectItem.value;
    List<String> result = new ArrayList<String>();

    if (BIRD.equals(value)) {
      result.addAll(Arrays.asList(DemoComplexForm.BIRDS));

    } else if (ANIMAL.equals(value)) {
      result.addAll(Arrays.asList(DemoComplexForm.ANIMALS));

    } else if (FISH.equals(value)) {
      result.addAll(Arrays.asList(DemoComplexForm.FISHES));

    } else if (DRAGON.equals(value)) {
      result.addAll(Arrays.asList(DemoComplexForm.DRAGONS));
    }

    return result;
  }

  // ==========================================================================================
  //  The data to show is defined here. The constants contain the keys to resolve labels,
  //  while the last part of the key following the last dot is used as the corresponding value
  // ==========================================================================================

  private static final String BIRD = "demo.complexForm.value.bird";

  private static final String ANIMAL = "demo.complexForm.value.animal";

  private static final String FISH = " demo.complexForm.value.fish";

  private static final String DRAGON = "demo.complexForm.value.dragon";

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

  public static class SelectItem {

    private String value;

    private String label;

    public SelectItem(String label) {
      this.label = label;
      this.value = StringUtils.substringAfterLast(label, ".");
    }

    public String getLabel() {
      return this.label;
    }

    public String getValue() {
      return this.value;
    }

    public String getDescription() {
      String result = "";
      if (BIRD.equals(this.value)) {
        result = "demo.complexForm.desc.bird";
      } else if (ANIMAL.equals(this.value)) {
        result = "demo.complexForm.desc.animal";
      } else if (FISH.equals(this.value)) {
        result = "demo.complexForm.desc.fish";
      } else if (DRAGON.equals(this.value)) {
        result = "demo.complexForm.desc.dragon";
      }
      return result;
    }
  }
}
