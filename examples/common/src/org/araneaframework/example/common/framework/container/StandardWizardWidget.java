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

package org.araneaframework.example.common.framework.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.StandardScope;
import org.araneaframework.example.common.framework.context.WizardContext;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * This widget may contain other widgets (called pages) as children. It routes <code>init()</code> and
 * <code>event()</code> to all of its children and render() to only current active child.
 * <p>
 * It receives following events:
 * <ol>
 * <li>eventId: "goto", eventParam: pageIndex.</li>
 * <li>eventId: "submit".</li>
 * <li>eventId: "cancel".</li>
 * </ol>
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class StandardWizardWidget extends BaseUIWidget implements WizardContext {

  private static final Log LOG = LogFactory.getLog(StandardWizardWidget.class);

  public static final String CURRENT_PAGE_KEY = "currentPage";

  // List of Widget objects
  private List<Widget> pages = new ArrayList<Widget>();

  // Active page index in the list
  private int currentPageIndex = 0;

  public int getCurrentPageIndex() {
    return currentPageIndex;
  }

  private void setCurrentPageIndex(int currentPageIndex) {
    if (this.currentPageIndex != currentPageIndex) {
      if (containsIndex(this.currentPageIndex)) {
        _getChildren().remove(CURRENT_PAGE_KEY);
      }
      this.currentPageIndex = currentPageIndex;
      _getChildren().put(CURRENT_PAGE_KEY, getPage(currentPageIndex));
    }
    makeListenersHandleGoto(getCurrentPage());
    LOG.debug("Current page index set to " + currentPageIndex);
  }

  // goto page

  private void assertTrue(boolean condition, String msg) {
    if (!condition) {
      throw new AraneaRuntimeException(msg);
    }
  }

  public void gotoNext() {
    assertTrue(countPages() > 0, "No pages found");
    assertTrue(getCurrentPageIndex() != countPages() - 1, "There are no more pages");
    setCurrentPageIndex(getCurrentPageIndex() + 1);
  }

  public void gotoPrevious() {
    assertTrue(countPages() > 0, "No pages found");
    assertTrue(getCurrentPageIndex() == 0, "There are no more pages");
    setCurrentPageIndex(getCurrentPageIndex() - 1);
  }

  public void gotoFirst() {
    assertTrue(countPages() > 0, "No pages found");
    setCurrentPageIndex(0);
  }

  public void gotoLast() {
    assertTrue(countPages() > 0, "No pages found");
    setCurrentPageIndex(countPages() - 1);
  }

  public void gotoPage(Widget page) {
    assertTrue(containsPage(page), "Page not found");
    setCurrentPageIndex(getIndexOfPage(page));
  }

  public void gotoPage(int index) {
    assertTrue(containsIndex(index), "Page index out of bounds, page index = " + index + ", total pages = "
        + countPages());
    setCurrentPageIndex(index);
  }

  // add/remove pages

  public void addPage(int index, Widget page) throws Exception {
    this.pages.add(index, page);
    initPage(page);
    LOG.debug("Page added, index = " + index);
  }

  public void addPage(Widget page) throws Exception {
    this.pages.add(page);
    initPage(page);
    LOG.debug("Page added, index = " + (countPages() - 1));
  }

  public void removePage(Widget page) throws Exception {
    int index = getIndexOfPage(page);
    destroyPage(page);
    this.pages.remove(page);
    if (getCurrentPageIndex() >= countPages()) {
      setCurrentPageIndex(countPages() > 0 ? countPages() - 1 : 0);
    }
    LOG.debug("Page removed, page index = " + index);
  }

  public void removePage(int index) throws Exception {
    removePage(getPage(index));
  }

  public void clearPages() throws Exception {
    for (Widget page : this.pages) {
      destroyPage(page);
    }
    this.pages.clear();
    this.currentPageIndex = 0;
    LOG.debug("All pages removed");
  }

  private void initPage(Widget page) throws Exception {
    LOG.debug("Initializing page...");
    page._getComponent().init(new StandardScope(CURRENT_PAGE_KEY, getScope()), getChildEnvironment());
    if (getIndexOfPage(page) == getCurrentPageIndex()) {
      _getChildren().put(CURRENT_PAGE_KEY, page);
    }
  }

  private void destroyPage(Widget page) throws Exception {
    LOG.debug("Destroying page...");
    if (getIndexOfPage(page) == getCurrentPageIndex() && _getChildren().containsKey(CURRENT_PAGE_KEY)) {
      _getChildren().remove(CURRENT_PAGE_KEY);
    }
  }

  // getters

  public Widget getPage(int index) {
    try {
      return this.pages.get(index);
    } catch (IndexOutOfBoundsException e) {
      throw new AraneaRuntimeException("Page index out of bounds, page index = " + index + ", total pages = "
          + countPages());
    }
  }

  public Widget[] getAllPages() {
    return this.pages.toArray(new Widget[this.pages.size()]);
  }

  public int getIndexOfPage(Widget page) {
    int index = this.pages.indexOf(page);
    if (index == -1) {
      throw new AraneaRuntimeException("Page not found");
    }
    return index;
  }

  public int countPages() {
    return this.pages.size();
  }

  public Widget getCurrentPage() {
    return getPage(getCurrentPageIndex());
  }

  // contains

  public boolean containsPage(Widget page) {
    return this.pages.contains(page);
  }

  public boolean containsIndex(int index) {
    return countPages() > 0 && index >= 0 && index < countPages();
  }

  @Override
  protected void render(OutputData output) throws Exception {
    LOG.debug("StandardWizardWidget render called");
    getCurrentPage()._getWidget().render(output);
  }

  @Override
  protected void destroy() throws Exception {
    clearPages();
  }

  /*
   * Submit & cancel
   */

  public void submit() {
    makeListenersHandleSubmit();
  }

  public void cancel() {
    makeListenersHandleCancel();
  }

  /*
   * Event listeners
   */

  private Collection<WizardContext.EventListener> eventListeners = new LinkedList<WizardContext.EventListener>();

  public void addEventListener(EventListener listener) {
    this.eventListeners.add(listener);
  }

  public void removeEventListener(EventListener listener) {
    this.eventListeners.remove(listener);
  }

  public void clearEventListeners() {
    this.eventListeners.clear();
  }

  private void makeListenersHandleGoto(Widget page) {
    try {
      for (EventListener eventListener : this.eventListeners) {
        eventListener.onGoto(page);
      }
    } catch (Exception e) {
      throw new AraneaRuntimeException(e);
    }
  }

  private void makeListenersHandleSubmit() {
    try {
      for (EventListener eventListener : this.eventListeners) {
        eventListener.onSubmit();
      }
    } catch (Exception e) {
      throw new AraneaRuntimeException(e);
    }
  }

  private void makeListenersHandleCancel() {
    try {
      for (EventListener eventListener : this.eventListeners) {
        eventListener.onCancel();
      }
    } catch (Exception e) {
      throw new AraneaRuntimeException(e);
    }
  }

  /*
   * Methods for HandleEventProxyEventListener
   */

  /**
   * The handler for <em>goto</em> event. The parameter is passed from the client side.
   * 
   * @param The page number passed from the client-side.
   */
  public void handleEventGoto(String eventParameter) {
    gotoPage(Integer.parseInt(eventParameter));
  }

  public void handleEventSubmit() {
    submit();
  }

  public void handleEventCancel() {
    cancel();
  }
}
