package org.araneaframework.uilib.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.DisplayItemBuilder;
import org.junit.Test;

/**
 * @author Vassili Jakovlev (vassili@webmedia.ee)
 */
public class DisplayItemUtilTest {

  @Test
  public void buildDisplayItems() {
    Collection<TestObject> values = getTestObjects();
    List<DisplayItem> displayItems = DisplayItemUtil.buildDisplayItems(values, new TestObjectItemBuilder());

    assertEquals(4, displayItems.size());
    Iterator<DisplayItem> it = displayItems.iterator();
    for (TestObject value : values) {
      DisplayItem displayItem = it.next();
      assertEquals(value.id.toString(), displayItem.getValue());
      assertEquals(value.text.toString(), displayItem.getLabel());
    }
  }

  @Test
  public void buildOrderedDisplayItems() {
    Collection<TestObject> values = getTestObjects();
    List<DisplayItem> displayItems =
        DisplayItemUtil.buildOrderedDisplayItems(values, new TestObjectItemBuilder(), new TestObjectComparator());

    assertEquals(4, displayItems.size());
    for (int i = 1; i <= 4; i++) {
      assertEquals(String.valueOf(i), displayItems.get(i - 1).getValue());
    }
  }

  private Collection<TestObject> getTestObjects() {
    Collection<TestObject> testObjects = new ArrayList<TestObject>();
    testObjects.add(new TestObject(2L, "Two"));
    testObjects.add(new TestObject(4L, "Four"));
    testObjects.add(new TestObject(3L, "Three"));
    testObjects.add(new TestObject(1L, "One"));
    return testObjects;
  }

  private static class TestObjectItemBuilder implements DisplayItemBuilder<TestObject> {
    @Override
    public DisplayItem buildDisplayItem(TestObject t) {
      return new DisplayItem(t.id.toString(), t.text);
    }
  }

  private static class TestObjectComparator implements Comparator<TestObject> {

    @Override
    public int compare(TestObject fst, TestObject snd) {
      return fst.id.compareTo(snd.id);
    }

  }

  private static class TestObject {
    private Long id;
    private String text;

    public TestObject(Long id, String text) {
      this.id = id;
      this.text = text;
    }
  }

}
