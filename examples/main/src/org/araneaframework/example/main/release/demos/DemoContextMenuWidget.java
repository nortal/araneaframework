/**
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.example.main.release.demos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.araneaframework.InputData;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.release.features.ExampleData;
import org.araneaframework.example.main.release.features.ExampleData.Client;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.util.FormUtil;
import org.araneaframework.uilib.menu.ContextMenuItem;
import org.araneaframework.uilib.menu.ContextMenuWidget;
import org.araneaframework.uilib.menu.ContextMenuItem.ContextMenuEventEntry;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoContextMenuWidget extends TemplateBaseWidget {
	protected List friends = new ArrayList();

	private MemoryBasedListDataProvider dataProvider = new DataProvider();

	//Plays the role of a sequence
	private Long lastId =  new Long(0);

	{
		Random rn = new Random();
		List allSuggestions = new ArrayList();

		for (Iterator i = Arrays.asList(Locale.getISOCountries()).iterator(); i.hasNext(); ) {
			allSuggestions.add(new Locale("en", (String)i.next()).getDisplayCountry(Locale.ENGLISH));
		}

		for (int i = 0; i <  ExampleData.males.length; i++) {
			ExampleData.Client friend = new ExampleData.Client();
			friend.setForename(ExampleData.males[i]);
			friend.setId(lastId);
			friend.setSex("M");
			friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
			friend.setCountry((String)allSuggestions.get(rn.nextInt(allSuggestions.size())));
			friends.add(friend);
			lastId = new Long(lastId.longValue() + 1);
		}

		for (int i = 0; i <  ExampleData.females.length; i++) {
			ExampleData.Client friend = new ExampleData.Client();
			friend.setForename(ExampleData.females[i]);
			friend.setSex ("F");
			friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
			friend.setCountry((String)allSuggestions.get(rn.nextInt(allSuggestions.size())));
			friends.add(friend);
			lastId = new Long(lastId.longValue() + 1);
		}
	}

	/* Editable list. */ 
	private BeanListWidget list;
	/* Actual holder of editable list rows (resides inside EditableBeanListWidget).
     Look inside init() method to see where it comes from. */ 
	private BeanFormListWidget formList;

	protected void init() throws Exception {
		setViewSelector("release/demos/contextMenuDemo");

		createList();
		
		list.addWidget("cmenu", createListContextMenu());
	}

	private void createList() {
		list = new BeanListWidget(ExampleData.Client.class);
		addWidget("list", list);
		list.setOrderableByDefault(true);
		list.addField("sex", "sed.Sex").like();		
		list.addField("forename", "sed.Forename").like();
		list.addField("surname", "sed.Surname").like();
		list.addField("country", "common.Country").like();
		list.addField("dummy", null, false);

		list.setDataProvider(dataProvider);
	}

	private class DataProvider extends MemoryBasedListDataProvider {
		private static final long serialVersionUID = 1L;
		protected DataProvider() {
			super(ExampleData.Client.class);
		}
		public List loadData() throws Exception {
			return friends;
		}
	}
	
  private ContextMenuWidget createListContextMenu() throws Exception {
//    ButtonControl button = new ButtonControl();
//    button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testSimpleForm"));
    // add the button to form. As the button does not hold any value, Data will be null.

    ContextMenuItem menu = new ContextMenuItem();
    menu.addMenuItem(new ContextMenuItem("Modify", new ContextMenuEventEntry("modifyRecord", this, "cMenuparameterSupplier")));
    menu.addMenuItem(new ContextMenuItem("Change sex", new ContextMenuEventEntry("changeSex", this, "cMenuparameterSupplier")));
    menu.addMenuItem(new ContextMenuItem("Delete", new ContextMenuEventEntry("deleteRecord", this, "cMenuparameterSupplier")));

    ContextMenuWidget contextMenuWidget = new ContextMenuWidget(menu);
    
    return contextMenuWidget;
  }

  private void handleEventModifyRecord(String param) {
	  Client c = (Client) list.getRowFromRequestId(param);
  }
  
  private void handleEventChangeSex(String param) {
	  Client c = (Client) list.getRowFromRequestId(param);
	  if (c.getSex().equals("M")) c.setSex("F");  else c.setSex("M");
  }
  
  private void handleEventDeleteRecord(String param) {
	  Client c = (Client) list.getRowFromRequestId(param);
	  
  }
}
