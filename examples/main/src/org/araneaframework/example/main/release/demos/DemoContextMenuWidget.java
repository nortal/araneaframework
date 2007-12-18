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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.release.features.ExampleData;
import org.araneaframework.example.main.release.features.ExampleData.Client;
import org.araneaframework.framework.LocalizationContext.LocaleChangeListener;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.menu.ContextMenuItem;
import org.araneaframework.uilib.menu.ContextMenuWidget;
import org.araneaframework.uilib.menu.ContextMenuItem.ContextMenuEventEntry;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoContextMenuWidget extends TemplateBaseWidget implements LocaleChangeListener {
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
		
		getL10nCtx().addLocaleChangeListener(this);

		createList();
		attachContextMenu();
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
	
  private ContextMenuWidget createListContextMenu() {
    ContextMenuItem menu = new ContextMenuItem();
    menu.addMenuItem(new ContextMenuItem(getL10nCtx().localize("common.View"), new ContextMenuEventEntry("viewRecord", this, "cMenuparameterSupplier")));
    menu.addMenuItem(new ContextMenuItem(getL10nCtx().localize("context.menu.ChangeSex"), new ContextMenuEventEntry("changeSex", this, "cMenuparameterSupplier")));
    menu.addMenuItem(new ContextMenuItem(getL10nCtx().localize("common.Remove"), new ContextMenuEventEntry("deleteRecord", this, "cMenuparameterSupplier")));

    ContextMenuWidget contextMenuWidget = new ContextMenuWidget(menu);
    
    return contextMenuWidget;
  }
  
  public void onLocaleChange(Locale oldLocale, Locale newLocale) {
    attachContextMenu();
  }

  private void attachContextMenu() {
	list.addWidget("cmenu", createListContextMenu());
  }

  private void handleEventViewRecord(String param) {
	  Client c = (Client) list.getRowFromRequestId(param);
	  getFlowCtx().start(new ClientViewWidget(c));
  }
  
  private void handleEventChangeSex(String param) {
	  Client c = (Client) list.getRowFromRequestId(param);
	  if (c.getSex().equals("M")) c.setSex("F");  else c.setSex("M");
  }
  
  private void handleEventDeleteRecord(String param) {
	  final Client c = (Client) list.getRowFromRequestId(param);
	  friends.remove(CollectionUtils.find(friends, new BeanPropertyValueEqualsPredicate("id", c.getId())));
	  list.refresh();
  }
}
