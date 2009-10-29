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

package org.araneaframework.example.main.release.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DemoVO;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

/**
 * This is an example of component with a single list.
 */
public class SimpleListWidget extends TemplateBaseWidget {

  protected ListWidget<DemoVO> simpleList;

  protected void init() throws Exception {
    setViewSelector("release/features/simpleList");

    this.simpleList = new BeanListWidget<DemoVO>(DemoVO.class);
    addWidget("simpleList", simpleList);
    this.simpleList.setDataProvider(new SimpleListDataProvider());
    this.simpleList.setOrderableByDefault(true);
    this.simpleList.addField("booleanValue", "#Boolean");
    this.simpleList.addField("stringValue", "#String");
    this.simpleList.addField("longValue", "#Long");
    this.simpleList.setInitialOrder("longValue", true);
  }

  private static class SimpleListDataProvider extends MemoryBasedListDataProvider<DemoVO> {

    protected List<DemoVO> data = new ArrayList<DemoVO>();

    public SimpleListDataProvider() {
      super(DemoVO.class);
      Random rnd = new Random();

      for (int i = 0; i < 100; i++) {
        DemoVO test1 = new DemoVO();
        test1.setId(new Long(1 + i));
        test1.setBooleanValue(Boolean.TRUE);
        test1.setStringValue(new Locale("en", Locale.getISOCountries()[i]).getDisplayCountry(Locale.ENGLISH));
        test1.setLongValue(new Long(rnd.nextLong() % 100));
        this.data.add(test1);

        DemoVO test2 = new DemoVO();
        test2.setId(new Long(2 + i));
        test2.setBooleanValue(Boolean.TRUE);
        test2.setStringValue("Peculiar");
        test2.setLongValue(new Long(rnd.nextLong() % 100));
        this.data.add(test2);

        DemoVO test3 = new DemoVO();
        test3.setId(new Long(3 + i));
        test3.setBooleanValue(Boolean.FALSE);
        test3.setStringValue("Queer");
        test3.setLongValue(new Long(rnd.nextLong() % 100));
        this.data.add(test3);
      }
    }

    public List<DemoVO> loadData() {
      return this.data;
    }
  }

  public void handleEventReturn() throws Exception {
    getFlowCtx().cancel();
  }
}
