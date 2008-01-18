package org.araneaframework.example.main.release.features;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Scope;
import org.araneaframework.core.ApplicationComponent;
import org.araneaframework.core.EventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.release.features.ExampleData.Attendee;
import org.araneaframework.example.main.release.features.ExampleData.Room;
import org.araneaframework.framework.LocalizationContext.LocaleChangeListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.data.StringListData;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SeamlessFormValidationDemoWidget extends TemplateBaseWidget {
	private SelectControl roomSelect;
	private List roomList;
	
	private MultiSelectControl attendeeMultiSelect;
	private List attendeeList;

	protected void init() throws Exception {
		roomList = ExampleData.createRooms();
		attendeeList = ExampleData.createAttendees();
		
		setViewSelector("release/features/seamlessFormValidation/demo");
		addWidget("form1", buildFormWidget());
		FormWidget f2 = buildFormWidget();
		
		// and here the crucial part for second form -- enabling background validation
		f2.setBackgroundValidation(true);
		addWidget("form2", f2);
	}

	private FormWidget buildFormWidget() {
		final FormWidget form = new FormWidget();
		final FormElement appointmentDate = form.addElement("futureDate", "seamless.appointmentdate", new DateControl(), new DateData(), true);
		final FormElement appointmentTime = form.addElement("time", "seamless.appointmenttime", new TimeControl(), new DateData(), true);
		
		FormElement rooms = form.addElement("meetingroom","seamless.room", buildRoomSelect(), new StringData(), true);
		rooms.setConstraint(new BaseFieldConstraint() {
			protected void validateConstraint() throws Exception {
				if (!appointmentDate.isValid() || !appointmentTime.isValid()) return;

				String value = (String) getValue();
				Room appointmentRoom = (Room) roomList.get(Integer.valueOf(value).intValue());
				if (appointmentRoom.isOccupied()) {
					addError(MessageUtil.localizeAndFormat(
							"seamless.room.not.available", 
							MessageUtil.localize(appointmentRoom.getName(), getEnvironment()), 
							getEnvironment()));
				}
			}
		});

		FormElement attendees = form.addElement("attendees", "seamless.attendees", buildAttendeeMultiSelect(), new StringListData(), true);
		attendees.setConstraint(new BaseFieldConstraint() {
			protected void validateConstraint() throws Exception {
				if (!appointmentDate.isValid() || !appointmentTime.isValid()) return;
				List preoccupiedAttendees = new ArrayList();
				List values = (List) getValue();
				for (Iterator i = values.iterator(); i.hasNext();) {
					Attendee attendee = (Attendee) attendeeList.get(Integer.valueOf((String) i.next()).intValue());
					if (attendee.isPreoccupied()) {
						preoccupiedAttendees.add(attendee);
					}
				}

				if (!preoccupiedAttendees.isEmpty()) {
					StringBuffer names = new StringBuffer();
					for (Iterator i = preoccupiedAttendees.iterator(); i.hasNext();) {
						Attendee attendee = (Attendee) i.next();
						names.append(attendee.getName());
						if (i.hasNext()) names.append(", ");
					}
					
					addError(MessageUtil.localizeAndFormat(
								"seamless.attendees.not.available", 
								names.toString(), 
								getEnvironment()));
				}
			}
		});

		form.addEventListener("submit", new EventListener() {
			public void processEvent(Object eventId, InputData input) throws Exception {
				form.convertAndValidate();
			}
		});

		return form;
	}

	private void handleEventReturn() {
      getFlowCtx().cancel();
	}

	private SelectControl buildRoomSelect() {
		final SelectControl result = new SelectControl();

		addRooms(result);

		getL10nCtx().addLocaleChangeListener(new LocaleChangeListenerAdapter(SeamlessFormValidationDemoWidget.this) {
			public void onLocaleChange(Locale oldLocale, Locale newLocale) {
				result.clearItems();
				addRooms(result);
			}
		});

		return result;
	}

	private void addRooms(SelectControl result) {
		for (int i = 0; i < roomList.size(); i++) {
			Room room = (Room)roomList.get(i);
			result.addItem(new DisplayItem(String.valueOf(i), getL10nCtx().localize(room.getName())+ ", " + room.getLocation()));
		}
	}
	
	private MultiSelectControl buildAttendeeMultiSelect() {
		MultiSelectControl result = new MultiSelectControl();
		for (int i = 0; i < attendeeList.size(); i++) {
			result.addItem(new DisplayItem(String.valueOf(i), ((Attendee)attendeeList.get(i)).getName()));
		}
		
		return result;
	}
	
	private abstract class LocaleChangeListenerAdapter implements LocaleChangeListener {
		private ApplicationComponent wrapped;
		
		public LocaleChangeListenerAdapter(ApplicationComponent wrapped) {
			this.wrapped = wrapped;
		}

		public Component.Interface _getComponent() {
			return wrapped._getComponent();
		}

		public org.araneaframework.Composite.Interface _getComposite() {
			return wrapped._getComposite();
		}

		public org.araneaframework.Viewable.Interface _getViewable() {
			return wrapped._getViewable();
		}

		public Environment getChildEnvironment() {
			return wrapped.getChildEnvironment();
		}

		public Environment getEnvironment() {
			return wrapped.getEnvironment();
		}

		public Scope getScope() {
			return wrapped.getScope();
		}

		public boolean isAlive() {
			return wrapped.isAlive();
		}
	}
}
