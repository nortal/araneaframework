package org.araneaframework.tests.uilib.tab;

import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.core.MockBaseWidget;
import org.araneaframework.uilib.tab.Tab;
import org.araneaframework.uilib.tab.TabContainerWidget;

public class TabContainerWidgetTest extends TestCase {

  private TabContainerWidget widget;

  public void setUp() {
    widget = new TabContainerWidget();
  }

  public void testCreateWidgetWithNoTab() throws Exception {
    MockLifeCycle.begin(widget);

    widget = new TabContainerWidget();
    widget.addTab(new TabTest.Tab("id1", "label1"));
    MockLifeCycle.begin(widget);

  }

  public void testSelectedTabInEmptyWidget() {
    MockLifeCycle.begin(widget);
    assertNull(widget.getSelectedTab());
  }

  public void testAddSelectInit() {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    TabTest.Tab tab2 = new TabTest.Tab("id2", "label2");
    widget.addTab(tab2);
    widget.addTab(new TabTest.Tab("id3", "label3"));

    try {
      widget.selectTab("id2");
      fail("We cannot select tabs prior to initialization container widget");
    } catch (IllegalStateException ex) {
      assertTrue(true);
    }
    
    MockLifeCycle.begin(widget);
    Tab selectedTab = widget.selectTab("id2");
    assertEquals("id2", widget.getSelectedTab().getId());
    assertEquals(selectedTab, widget.getSelectedTab());
    assertEquals(selectedTab, tab2);
  }

  public void testAddInitSelect() throws Exception {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    MockLifeCycle.begin(widget);

    Tab selectTab = widget.selectTab("id1");
    assertEquals("id1", widget.getSelectedTab().getId());
    assertEquals(selectTab, widget.getSelectedTab());
    assertTrue(widget.getSelectedWidget() instanceof MockBaseWidget);

    selectTab = widget.selectTab("id2");
    assertEquals("id2", widget.getSelectedTab().getId());
    assertEquals(selectTab, widget.getSelectedTab());

    selectTab = widget.selectTab(null);
    assertNull(widget.getSelectedTab());
    assertNull(selectTab);

  }

  public void testSelectNonExistingTab() {
    try {
      widget.selectTab("id10");
      fail("Cannot select non-existing tab!");
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    widget.addTab(new TabTest.Tab("id2", "label2"));
    try {
      widget.selectTab("id10");
      fail("Cannot select non-existing tab!");
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    MockLifeCycle.begin(widget);
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

    widget.addTab(new TabTest.Tab("id1", "label1"));
    assertEquals(1, widget.getTabs().size());
  }

  public void testRemoveTab() throws Exception {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    MockLifeCycle.begin(widget);

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

  public void testHandleEventSelect() throws Exception {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    MockLifeCycle.begin(widget);

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
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  public void testLifeCycle() {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    MockLifeCycle.begin(widget);

    assertEquals("id1", widget.getSelectedTab().getId());
    MockLifeCycle.end(widget);
    assertNull(widget.getSelectedTab());
    assertNull(widget.getSelectedWidget());
  }

  public void testGetTabs() {
    widget.addTab(new TabTest.Tab("id1", "label1"));
    widget.addTab(new TabTest.Tab("id2", "label2"));
    widget.addTab(new TabTest.Tab("id3", "label3"));

    MockLifeCycle.begin(widget);

    Map tabs = widget.getTabs();
    assertEquals(3, tabs.size());

    tabs.put("qq1", "wwww1");
    tabs.put("qq2", "wwww2");

    tabs = widget.getTabs();
    assertEquals(3, tabs.size());
    assertNull(tabs.get("qq1"));
  }
}
