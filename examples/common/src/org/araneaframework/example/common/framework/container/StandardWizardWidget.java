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

package org.araneaframework.example.common.framework.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.StandardScope;
import org.araneaframework.example.common.framework.context.WizardContext;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * This widget may contain other widgets (called pages) as children.
 * It routes init() and event() to all of its children
 * and process() and render() to only current active child.
 * 
 * It recieves following events:
 * 1. eventId: "goto", eventParam: pageIndex.
 * 2. eventId: "submit".
 * 3. eventId: "cancel".
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class StandardWizardWidget extends BaseUIWidget implements WizardContext {
	
	private static final Logger log = Logger.getLogger(StandardWizardWidget.class);
	
	public static final String CURRENT_PAGE_KEY = "currentPage";
	
	// List of Widget objects
	private List pages = new ArrayList();
	
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
		log.debug("Current page index set to " + currentPageIndex);
	}
	
	// goto page
	
	public void gotoNext() {
		if (countPages() == 0) {
			throw new AraneaRuntimeException("No pages found");
		}
		if (getCurrentPageIndex() == countPages() - 1) {
			throw new AraneaRuntimeException("There are no more pages");
		}
		setCurrentPageIndex(getCurrentPageIndex() + 1);
	}
	
	public void gotoPrevious() {
		if (countPages() == 0) {
			throw new AraneaRuntimeException("No pages found");
		}
		if (getCurrentPageIndex() == 0) {
			throw new AraneaRuntimeException("There are no more pages");
		}
		setCurrentPageIndex(getCurrentPageIndex() - 1);
	}
	
	public void gotoFirst() {
		if (countPages() == 0) {
			throw new AraneaRuntimeException("No pages found");
		}
		setCurrentPageIndex(0);
	}
	public void gotoLast() {
		if (countPages() == 0) {
			throw new AraneaRuntimeException("No pages found");
		}
		setCurrentPageIndex(countPages() - 1);
	}
	
	public void gotoPage(Widget page) {
		if (!containsPage(page)) {
			throw new AraneaRuntimeException("Page not found");
		}
		setCurrentPageIndex(getIndexOfPage(page));
	}
	
	public void gotoPage(int index) {
		if (!containsIndex(index)) {
			throw new AraneaRuntimeException("Page index out of bounds, page index = " + index + ", total pages = " + countPages());
		}
		setCurrentPageIndex(index);
	}
	
	// add/remove pages
	
	public void addPage(int index, Widget page) throws Exception {
		pages.add(index, page);
		initPage(page);
		log.debug("Page added, index = " + index);
	}
	
	public void addPage(Widget page) throws Exception {
		pages.add(page);
		initPage(page);
		log.debug("Page added, index = " + (countPages()-1));
	}
	
	public void removePage(Widget page) throws Exception {	
		int index = getIndexOfPage(page);
		destroyPage(page);
		pages.remove(page);
		if (getCurrentPageIndex() >= countPages()) {
			setCurrentPageIndex(countPages() > 0 ? countPages() - 1 : 0);
		}
		log.debug("Page removed, page index = " + index);
	}
	
	public void removePage(int index) throws Exception {
		removePage(getPage(index));
	}
	
	public void clearPages() throws Exception {
		for (Iterator i = pages.iterator(); i.hasNext();) {
			destroyPage((Widget) i.next());
		}
		pages.clear();
		currentPageIndex = 0;
		log.debug("All pages removed");
	}
	
	private void initPage(Widget page) throws Exception {
		log.debug("Initializing page...");
		page._getComponent().init(new StandardScope(CURRENT_PAGE_KEY, getScope()), getChildEnvironment());
		if (getIndexOfPage(page) == getCurrentPageIndex()) {
			_getChildren().put(CURRENT_PAGE_KEY, page);
		}
	}
	
	private void destroyPage(Widget page) throws Exception {
		log.debug("Destroying page...");
		if (getIndexOfPage(page) == getCurrentPageIndex()
				&& _getChildren().containsKey(CURRENT_PAGE_KEY)) {
			_getChildren().remove(CURRENT_PAGE_KEY);
		}
		page._getComponent().destroy();
	}
	
	// getters
	
	public Widget getPage(int index) {		
		try {
			return (Widget) pages.get(index);
		}
		catch (IndexOutOfBoundsException e) {
			throw new AraneaRuntimeException("Page index out of bounds, page index = " + index + ", total pages = " + countPages());
		}
	}
	
	public Widget[] getAllPages() {
		return (Widget[]) pages.toArray(new Widget[pages.size()]);
	}
	
	public int getIndexOfPage(Widget page) {
		int index = pages.indexOf(page);
		if (index == -1) {
			throw new AraneaRuntimeException("Page not found");
		}
		return index;
	}
	
	public int countPages() {
		return pages.size();
	}
	
	public Widget getCurrentPage() {
		return getPage(getCurrentPageIndex());
	}
	
	// contains
	
	public boolean containsPage(Widget page) {
		return pages.contains(page);	
	}
	
	public boolean containsIndex(int index) {
		return countPages() > 0 && index >= 0 && index < countPages();  
	}
	
	protected void render(OutputData output) throws Exception {
		log.debug("StandardWizardWidget render called");    
		output.pushScope(CURRENT_PAGE_KEY);
		try {
			getCurrentPage()._getWidget().render(output);
		}
		finally {
			output.popScope();
		}    
	}
	
	protected void destroy() throws Exception {
		clearPages();		
		super.destroy();
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

	private Collection eventListeners = new LinkedList();
	
	public void addEventListener(WizardContext.EventListener listener) {
		eventListeners.add(listener);
	}
	public void removeEventListener(WizardContext.EventListener listener) {
		eventListeners.remove(listener);
	}
	public void clearEventListeners() {
		eventListeners.clear();
	}
	
	private void makeListenersHandleGoto(Widget page) {
		try {
			for (Iterator i = eventListeners.iterator(); i.hasNext();) {
				((WizardContext.EventListener) i.next()).onGoto(page);
			}
		} catch (Exception e) {
			throw new AraneaRuntimeException(e);
		}
	}	
	private void makeListenersHandleSubmit() {
		try {
			for (Iterator i = eventListeners.iterator(); i.hasNext();) {
				((WizardContext.EventListener) i.next()).onSubmit();
			}
		} catch (Exception e) {
			throw new AraneaRuntimeException(e);
		}
	}	
	private void makeListenersHandleCancel() {
		try {
			for (Iterator i = eventListeners.iterator(); i.hasNext();) {
				((WizardContext.EventListener) i.next()).onCancel();
			}
		} catch (Exception e) {
			throw new AraneaRuntimeException(e);
		}
	}
	
	/*
	 * Methods for HandleEventProxyEventListener 
	 */
	
	public void handleEventGoto(String eventParameter) throws Exception {
		gotoPage(Integer.parseInt(eventParameter));		
	}	
	public void handleEventSubmit(String eventParameter) throws Exception {
		submit();
	}	
	public void handleEventCancel(String eventParameter) throws Exception {
		cancel();
	}
}
