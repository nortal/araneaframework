package org.araneaframework.tests.uilib.tab;

import junit.framework.TestCase;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.tab.Tab;
import org.araneaframework.uilib.tab.TabContainerWidget;

public class TabContainerWidgetTest extends TestCase {

  private TabContainerWidget widget;

  public void setUp() {
    widget = new TabContainerWidget();
  }

  public void testCreateWidgetWithNoTab() {
    try {
      widget._getComponent().init(new MockEnvironment());
      fail("Cannot initialize TabWidgetContainer without any tabs.");
    } catch (IllegalStateException ex) {
      assertTrue(true);
    }

    widget = new TabContainerWidget();
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget._getComponent().init(new MockEnvironment());

  }

  public void testSelectTab() {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    widget._getComponent().init(new MockEnvironment());

    Tab selectTab = widget.selectTab("id1");
    assertEquals("id1", widget.getSelectedTab().getId());
    assertEquals(selectTab, widget.getSelectedTab());

    selectTab = widget.selectTab("id2");
    assertEquals("id2", widget.getSelectedTab().getId());
    assertEquals(selectTab, widget.getSelectedTab());

    selectTab = widget.selectTab(null);
    assertNull(widget.getSelectedTab());
    assertNull(selectTab);

    try {
      widget.selectTab("id10");
      fail("Cannot select non-existing tab!");
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

  }

  public void testAddTab() {
    try {
      widget.addTab(null);
      fail("Cannot add null tab");
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    widget.addTab(new TabTest.Tab("id1", "label1"));

    try {
      widget.addTab(new TabTest.Tab("id1", "label1"));
      fail("Such tab already exists");
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  public void testRemoveTab() {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    widget._getComponent().init(new MockEnvironment());

    widget.selectTab("id1");

    Tab tab = widget.removeTab("id2");
    assertEquals("id1", widget.getSelectedTab().getId());
    assertEquals("id2", tab.getId());

    tab = widget.removeTab("id1");
    assertNull(widget.getSelectedTab());
    assertEquals("id1", tab.getId());

    tab = widget.removeTab("id10");
    assertNull(tab);
  }

  public void testHandleEventSelect() {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    widget._getComponent().init(new MockEnvironment());
    
    widget.handleEventSelect("id1");
    assertEquals("id1", widget.getSelectedTab().getId());

    widget.handleEventSelect("id1");
    assertEquals("id1", widget.getSelectedTab().getId());
    
    widget.handleEventSelect("id3");
    assertEquals("id3", widget.getSelectedTab().getId());
    
    widget.handleEventSelect("");
    assertNull(widget.getSelectedTab());
    
    try {
      widget.handleEventSelect("id5");
      fail("No such tab to select");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
    
  }

}
