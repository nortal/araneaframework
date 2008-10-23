/**
 * Copyright 2007 Webmedia Group Ltd. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.menu.ContextMenuItem;
import org.araneaframework.uilib.menu.ContextMenuWidget;
import org.araneaframework.uilib.menu.ContextMenuItem.ContextMenuEventEntry;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoContextMenuWidget extends TemplateBaseWidget
  implements LocaleChangeListener {

  private static final long serialVersionUID = 1L;

  protected List friends = new ArrayList();

  private MemoryBasedListDataProvider dataProvider = new DataProvider();

  // Plays the role of a sequence
  private Long lastId = new Long(0);
  {
    Random rn = new Random();
    List allSuggestions = new ArrayList();
    for (Iterator i = Arrays.asList(Locale.getISOCountries()).iterator(); i
        .hasNext();) {
      allSuggestions.add(new Locale("en", (String) i.next())
          .getDisplayCountry(Locale.ENGLISH));
    }
    for (int i = 0; i < ExampleData.males.length; i++) {
      ExampleData.Client friend = new ExampleData.Client();
      friend.setForename(ExampleData.males[i]);
      friend.setId(this.lastId);
      friend.setSex("M");
      friend
          .setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
      friend.setCountry((String) allSuggestions.get(rn.nextInt(allSuggestions
          .size())));
      this.friends.add(friend);
      this.lastId = new Long(this.lastId.longValue() + 1);
    }
    for (int i = 0; i < ExampleData.females.length; i++) {
      ExampleData.Client friend = new ExampleData.Client();
      friend.setForename(ExampleData.females[i]);
      friend.setSex("F");
      friend
          .setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
      friend.setCountry((String) allSuggestions.get(rn.nextInt(allSuggestions
          .size())));
      this.friends.add(friend);
      this.lastId = new Long(this.lastId.longValue() + 1);
    }
  }

  /* Editable list. */
  private BeanListWidget list;

  protected void init() throws Exception {
    setViewSelector("release/demos/contextMenuDemo");
    getL10nCtx().addLocaleChangeListener(this);
    createList();
    attachContextMenu();
  }

  private void createList() {
    this.list = new BeanListWidget(ExampleData.Client.class);
    addWidget("list", this.list);
    this.list.setOrderableByDefault(true);
    this.list.addField("sex", "sed.Sex").like();
    this.list.addField("forename", "sed.Forename").like();
    this.list.addField("surname", "sed.Surname").like();
    this.list.addField("country", "common.Country").like();
    this.list.addField("dummy", null, false);
    this.list.setDataProvider(this.dataProvider);
  }

  private class DataProvider extends MemoryBasedListDataProvider {

    private static final long serialVersionUID = 1L;

    protected DataProvider() {
      super(ExampleData.Client.class);
    }

    public List loadData() throws Exception {
      return DemoContextMenuWidget.this.friends;
    }
  }

  private ContextMenuWidget createListContextMenu() {
    ContextMenuItem menu = new ContextMenuItem();
    addMenuItem(menu, "common.View", "viewRecord");
    addMenuItem(menu, "context.menu.ChangeSex", "changeSex");
    addMenuItem(menu, "common.Remove", "deleteRecord");
    ContextMenuWidget contextMenuWidget = new ContextMenuWidget(menu);
    return contextMenuWidget;
  }

  private void addMenuItem(ContextMenuItem menu, String labelId, String eventId) {
    String label = getL10nCtx().localize(labelId);
    menu.addMenuItem(new ContextMenuItem(label, new ContextMenuEventEntry(
        eventId, this, "cMenuparameterSupplier")));
  }

  public void onLocaleChange(Locale oldLocale, Locale newLocale) {
    attachContextMenu();
  }

  private void attachContextMenu() {
    this.list.addWidget("contextmenu", createListContextMenu());
  }

  public void handleEventViewRecord(String param) {
    Client c = (Client) this.list.getRowFromRequestId(param);
    getFlowCtx().start(new ClientViewWidget(c));
    // XXX: this is a hack to work around the shortcoming of partial rendering
    // -- namely when flow is switched,
    // the regions that are supposed to be updated are of course lost and old
    // flow remains on end-user screen
    EnvironmentUtil.getUpdateRegionContext(getEnvironment()).disableOnce();
  }

  public void handleEventChangeSex(String param) {
    Client c = (Client) this.list.getRowFromRequestId(param);
    if (c.getSex().equals("M")) {
      c.setSex("F");
    } else {
      c.setSex("M");
    }
  }

  public void handleEventDeleteRecord(String param) {
    final Client c = (Client) this.list.getRowFromRequestId(param);
    this.friends.remove(CollectionUtils.find(this.friends,
        new BeanPropertyValueEqualsPredicate("id", c.getId())));
    this.list.refresh();
  }
}
