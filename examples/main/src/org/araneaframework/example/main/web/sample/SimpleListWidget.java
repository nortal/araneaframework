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

package org.araneaframework.example.main.web.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.TestVO;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

/**
 * This is an example of component with a single list.
 */
public class SimpleListWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(SimpleListWidget.class);

  protected ListWidget simpleList;
    
  protected void init() throws Exception {
	setViewSelector("sample/simpleList");
	
	simpleList = new ListWidget();
	addWidget("simpleList", simpleList);
	simpleList.setDataProvider(new SimpleListDataProvider());
	simpleList.addField("booleanValue", "#Boolean");
	simpleList.addField("stringValue", "#String");
	simpleList.addField("longValue", "#Long");
	simpleList.setInitialOrder("longValue", true);
  }  
  
  private static class SimpleListDataProvider extends MemoryBasedListDataProvider {
    private static final long serialVersionUID = 1L;
    protected List data = new ArrayList();
    
    public SimpleListDataProvider() {
      super(TestVO.class);
      Random rnd = new Random();
      
      for(int i = 0; i < 100; i++) {
        TestVO test1 = new TestVO();
        test1.setId(new Long(1 + i));
        test1.setBooleanValue(Boolean.TRUE);
        test1.setStringValue("Strange");
        test1.setLongValue(new Long(rnd.nextLong() % 100));
        data.add(test1);
    
        TestVO test2 = new TestVO();
        test2.setId(new Long(2 + i));
        test2.setBooleanValue(Boolean.TRUE);
        test2.setStringValue("Peculiar");
        test2.setLongValue(new Long(rnd.nextLong() % 100));
        data.add(test2);
    
        TestVO test3 = new TestVO();
        test3.setId(new Long(3 + i));
        test3.setBooleanValue(Boolean.FALSE);
        test3.setStringValue("Queer");
        test3.setLongValue(new Long(rnd.nextLong() % 100));
        data.add(test3);
      }      
    }    

    public List loadData() {
      return data;
    }
  }
  
  public void handleEventReturn(String eventParameter) throws Exception {
	  getFlowCtx().cancel();
  }	
}
