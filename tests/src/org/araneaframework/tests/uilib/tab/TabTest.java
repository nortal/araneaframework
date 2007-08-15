package org.araneaframework.tests.uilib.tab;

import junit.framework.TestCase;
import org.araneaframework.Widget;
import org.araneaframework.mock.core.MockBaseWidget;

public class TabTest extends TestCase {

  public void testEquals() {
    Tab tab1 = new Tab("id1", "label");
    assertFalse(tab1.equals(null));
    assertFalse(tab1.equals("id1"));
    
    Tab tab2 = new Tab("id1", null);
    assertEquals(tab1, tab2);
    assertEquals(tab1.hashCode(), tab2.hashCode());
    
    Tab tab3 = new Tab("id3", "label");
    assertFalse(tab1.equals(tab3));
    assertFalse(tab1.hashCode() == tab3.hashCode());
  }
  
  public void testConstructor() {
    try {
      new Tab(null, null);
      fail("Cannot create tab without id.");
    }catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
    try {
      new Tab(null, "label");
      fail("Cannot create tab without id.");
    }catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
    try {
      new Tab("", "label");
      fail("Cannot create tab with empy id.");
    }catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    new Tab("id", null);
  }
  
  protected static class Tab extends org.araneaframework.uilib.tab.Tab {

    public Tab(String id, String labelId) {
      super(id, labelId);
    }

    public Widget createWidget() {
      return new MockBaseWidget();
    }
    
  }
}
