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

package org.araneaframework.example.main.web.release.contextMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.release.model.ExampleData;
import org.araneaframework.example.main.web.release.model.ExampleData.Client;
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
public class DemoContextMenuWidget extends TemplateBaseWidget implements LocaleChangeListener {

  /* Editable list. */
  private BeanListWidget<ExampleData.Client> list;

  protected List<ExampleData.Client> friends = new ArrayList<ExampleData.Client>();

  private MemoryBasedListDataProvider<ExampleData.Client> dataProvider = new DataProvider();

  // Plays the role of a sequence
  private Long lastId = 0L;

  
  public DemoContextMenuWidget() {
    Random rn = new Random();
    List<String> allSuggestions = new ArrayList<String>();

    for (String country  : Locale.getISOCountries()) {
      allSuggestions.add(new Locale("en", country).getDisplayCountry(Locale.ENGLISH));
    }

    for (String male : ExampleData.males) {
      ExampleData.Client friend = new ExampleData.Client();
      friend.setForename(male);
      friend.setId(this.lastId);
      friend.setSex(ExampleData.Client.SEX_M);
      friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
      friend.setCountry(allSuggestions.get(rn.nextInt(allSuggestions.size())));
      this.friends.add(friend);
      this.lastId = this.lastId++;
    }

    for (String female : ExampleData.females) {
      ExampleData.Client friend = new ExampleData.Client();
      friend.setForename(female);
      friend.setSex(ExampleData.Client.SEX_F);
      friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
      friend.setCountry(allSuggestions.get(rn.nextInt(allSuggestions.size())));
      this.friends.add(friend);
      this.lastId = this.lastId++;
    }
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("release/contextMenu/clientList");
    getL10nCtx().addLocaleChangeListener(this);
    createList();
    attachContextMenu();
  }

  private void createList() {
    this.list = new BeanListWidget<ExampleData.Client>(ExampleData.Client.class);
    this.list.setOrderableByDefault(true);
    this.list.addField("sex", "common.sex").like();
    this.list.addField("forename", "common.firstname").like();
    this.list.addField("surname", "common.lastname").like();
    this.list.addField("country", "common.Country").like();
    this.list.addField("dummy", null, false);
    this.list.setDataProvider(this.dataProvider);
    addWidget("list", this.list);
  }

  private class DataProvider extends MemoryBasedListDataProvider<ExampleData.Client> {

    protected DataProvider() {
      super(ExampleData.Client.class);
    }

    @Override
    public List<ExampleData.Client> loadData() throws Exception {
      return DemoContextMenuWidget.this.friends;
    }
  }

  private ContextMenuWidget createListContextMenu() {
    ContextMenuItem menu = new ContextMenuItem();
    addMenuItem(menu, "button.view", "viewRecord");
    addMenuItem(menu, "contextMenu.changeSex", "changeSex");
    addMenuItem(menu, "button.remove", "deleteRecord");
    ContextMenuWidget contextMenuWidget = new ContextMenuWidget(menu);
    return contextMenuWidget;
  }

  private void addMenuItem(ContextMenuItem menu, String labelId, String eventId) {
    String label = getL10nCtx().localize(labelId);
    menu.addMenuItem(new ContextMenuItem(label, new ContextMenuEventEntry(eventId, this, "cMenuparameterSupplier")));
  }

  public void onLocaleChange(Locale oldLocale, Locale newLocale) {
    attachContextMenu();
  }

  private void attachContextMenu() {
    this.list.addWidget("contextmenu", createListContextMenu());
  }

  public void handleEventViewRecord(String param) {
    Client c = this.list.getRowFromRequestId(param);
    getFlowCtx().start(new ClientViewWidget(c));

    // XXX: this is a hack to work around the shortcoming of partial rendering -- namely when flow is switched,
    // the regions that are supposed to be updated are of course lost and old flow remains on end-user screen
    EnvironmentUtil.getUpdateRegionContext(getEnvironment()).disableOnce();
  }

  public void handleEventChangeSex(String param) {
    Client c = this.list.getRowFromRequestId(param);
    c.setSex(c.getSex() == ExampleData.Client.SEX_M ? ExampleData.Client.SEX_F : ExampleData.Client.SEX_M);
  }

  public void handleEventDeleteRecord(String param) {
    final Client c = this.list.getRowFromRequestId(param);
    this.friends.remove(CollectionUtils.find(this.friends, new BeanPropertyValueEqualsPredicate("id", c.getId())));
    this.list.refresh();
  }
}
