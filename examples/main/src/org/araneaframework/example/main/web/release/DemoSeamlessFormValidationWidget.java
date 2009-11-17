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

package org.araneaframework.example.main.web.release;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.InputData;
import org.araneaframework.core.EventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.release.model.ExampleData;
import org.araneaframework.example.main.web.release.model.ExampleData.Attendee;
import org.araneaframework.example.main.web.release.model.ExampleData.Room;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.ListData;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoSeamlessFormValidationWidget extends TemplateBaseWidget {

  private List<Room> roomList;

  private List<Attendee> attendeeList;

  @Override
  protected void init() throws Exception {
    this.roomList = ExampleData.createRooms();
    this.attendeeList = ExampleData.createAttendees();

    setViewSelector("release/features/seamlessFormValidation/demo");

    FormWidget f2 = buildFormWidget();
    f2.setBackgroundValidation(true); // and here the crucial part for the second form - enabling background validation.

    addWidget("form1", buildFormWidget());
    addWidget("form2", f2);
  }

  /**
   * This method builds a form widget with select and multi-select controls.
   * 
   * @return A form widget that is not already contained by other widget.
   */
  private FormWidget buildFormWidget() {

    final FormWidget form = new FormWidget();

    final FormElement<Timestamp, Date> appointmentDate = form.addElement("futureDate", "seamless.appointmentdate",
        new DateControl(), new DateData(), true);

    final FormElement<Timestamp, Date> appointmentTime = form.addElement("time", "seamless.appointmenttime",
        new TimeControl(), new DateData(), true);


    // Creating and adding the select control:

    FormElement<Room, Room> rooms = form.addElement("meetingroom", "seamless.room", buildRoomSelect(), new Data<Room>(
        Room.class), true);

    // Let's add a constraint to this select control that validates (when clicked) whether an item can be selected or
    // not. Preoccupied rooms cannot be selected, and therefore give an error to the user.

    rooms.setConstraint(new BaseFieldConstraint<Room, Room>() {

      @Override
      protected void validateConstraint() throws Exception {
        if (appointmentDate.isValid() && appointmentTime.isValid()) {
          Room appointmentRoom = getValue();
          if (appointmentRoom.isOccupied()) {
            addError(MessageUtil.localizeAndFormat(getEnvironment(), "seamless.room.not.available", t(appointmentRoom
                .getName())));
          }
        }
      }
    });


    // Creating and adding the multi-select control:

    FormElement<List<Attendee>, List<Attendee>> attendees = form.addElement("attendees", "seamless.attendees",
        buildAttendeeMultiSelect(), new ListData<Attendee>(Attendee.class), true);

    // Let's add a constraint to this select control that validates (when clicked) whether an item can be selected or
    // not. Preoccupied attendees cannot be selected, and therefore give an error to the user.

    attendees.setConstraint(new BaseFieldConstraint<List<Attendee>, List<Attendee>>() {

      @Override
      protected void validateConstraint() throws Exception {
        if (appointmentDate.isValid() && appointmentTime.isValid()) {
          List<Attendee> preoccupiedAttendees = new ArrayList<Attendee>();

          for (Attendee attendee : getValue()) {
            if (attendee.isPreoccupied()) {
              preoccupiedAttendees.add(attendee);
            }
          }

          if (!preoccupiedAttendees.isEmpty()) {
            String names = StringUtils.join(preoccupiedAttendees, ", ");
            addError(MessageUtil.localizeAndFormat(getEnvironment(), "seamless.attendees.not.available", names));
          }
        }
      }
    });


    // Finally, let's add an event callback to our form that would respond to "submit" event where, form would be
    // converted and validated. Note that it is easier defining the callback this way rather than as a "handleEvent*()"
    // method because here the event is bound to this specific form (otherwise, we would have to figure out which form
    // was submitted by creating 2 different methods).

    form.addEventListener("submit", new EventListener() {

      public void processEvent(String eventId, InputData input) throws Exception {
        form.convertAndValidate();
      }
    });

    return form;
  }

  /**
   * Here's the second
   */
  public void handleEventReturn() {
    getFlowCtx().cancel();
  }

  /**
   * For convenience, the creation of <code>SelectControl</code> is encapsulated into this method.
   * 
   * @return The <code>SelectControl</code> with selectable rooms.
   */
  private SelectControl<Room> buildRoomSelect() {
    return new SelectControl<Room>(this.roomList, "name",  "id");
  }

  /**
   * For convenience, the creation of <code>MultiSelectControl</code> is encapsulated into this method.
   * 
   * @return The <code>MultiSelectControl</code> with selectable attendees.
   */
  private MultiSelectControl<Attendee> buildAttendeeMultiSelect() {
    return new MultiSelectControl<Attendee>(this.attendeeList, "name", "id");
  }
}
