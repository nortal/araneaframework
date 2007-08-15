package org.araneaframework.tests.uilib.tab;

import junit.framework.TestCase;
import org.araneaframework.Service;
import org.araneaframework.Widget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.tab.WidgetTab;

public class WidgetTabTest extends TestCase {

  public void testCreate() throws Exception {
    new WidgetTab("id", "label", ListWidget.class);
    
    try {
      new WidgetTab("id", "label", Service.class);
      fail("Should fail on non-Widget classes");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
    
    try {
      new WidgetTab("id", "label", Widget.class);
      fail("Should fail on widgets with no no-arg constructor");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }
}
