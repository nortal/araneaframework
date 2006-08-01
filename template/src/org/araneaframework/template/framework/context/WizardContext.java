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

package org.araneaframework.template.framework.context;

import java.io.Serializable;
import org.araneaframework.Widget;

/**
 * A context consisting of Widgets that could be switched through different
 * requests. Default page index is 0. <code>submit()</code>,
 * <code>cancel()</code> and all <code>gotoXXX()</code> methods invoke all
 * event listeners with respective method.
 * 
 * @see org.araneaframework.template.framework.container.StandardWizardWidget
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public interface WizardContext {

	/*
	 * Modify pages
	 */

	/**
	 * Adds new page with specific index (current page indexes are modified
	 * respectively). Wizard must be initialized before adding pages to it.
	 * 
	 * @param index
	 *          page index.
	 * @param page
	 *          page.
	 * @throws Exception
	 */
	void addPage(int index, Widget page) throws Exception;

	/**
	 * Adds new page as last element. Wizard must be initialized before adding
	 * pages to it.
	 * 
	 * @param page
	 *          page.
	 * @throws Exception
	 */
	void addPage(Widget page) throws Exception;

	/**
	 * Removes a page. Next (previous if this was the last one) page will be
	 * selected.
	 * 
	 * @param page
	 *          page.
	 * @throws Exception
	 */
	void removePage(Widget page) throws Exception;

	/**
	 * Removes a page. Next (previous if this was the last one) page will be
	 * selected.
	 * 
	 * @param index
	 *          page index.
	 * @throws Exception
	 */
	void removePage(int index) throws Exception;

	/**
	 * Removes all pages. Page index will be reset to 0.
	 * 
	 * @throws Exception
	 */
	void clearPages() throws Exception;

	/*
	 * Goto page
	 */

	/**
	 * Switch to the next page.
	 */
	void gotoNext();

	/**
	 * Switch to the previous page.
	 */
	void gotoPrevious();

	/**
	 * Switch to the first page.
	 */
	void gotoFirst();

	/**
	 * Switch to the last page.
	 */
	void gotoLast();

	/**
	 * Switch to the specified page.
	 * 
	 * @param page
	 *          page.
	 */
	void gotoPage(Widget page);

	/**
	 * Switch to a page with specified index.
	 * 
	 * @param index
	 *          page index.
	 */
	void gotoPage(int index);

	/*
	 * Read only methods
	 */

	/**
	 * Returns a page with specified index.
	 * 
	 * @param index
	 *          page index.
	 * @return page with specified index.
	 */
	Widget getPage(int index);

	/**
	 * Returns all pages.
	 * 
	 * @return all pages.
	 */
	Widget[] getAllPages();

	/**
	 * Returns an index of specified page.
	 * 
	 * @param page
	 *          page.
	 * @return page index.
	 */
	int getIndexOfPage(Widget page);

	/**
	 * Count all pages.
	 * 
	 * @return page count.
	 */
	int countPages();

	/**
	 * Returns whether the specified page has been added.
	 * 
	 * @param page
	 *          page.
	 * @return whether the specified page has been added.
	 */
	boolean containsPage(Widget page);

	/**
	 * Returns whether the specified index match the page index bounds.
	 * 
	 * @param index
	 *          page index.
	 * @return whether the specified index match the page index bounds.
	 */
	boolean containsIndex(int index);

	/**
	 * Returns the index of current page.
	 * 
	 * @return page index.
	 */
	int getCurrentPageIndex();

	/**
	 * Returns the current page.
	 * 
	 * @return page.
	 */
	Widget getCurrentPage();

	/*
	 * Submit & cancel
	 */

	/**
	 * Calls onSubmit() on eventListeners.
	 */
	void submit();

	/**
	 * Calls onCancel() on eventListeners.
	 */
	void cancel();

	/*
	 * Event listeners
	 */

	/**
	 * Adds new event listener.
	 * 
	 * @param listener
	 *          listener.
	 */
	void addEventListener(EventListener listener);

	/**
	 * Removes a specified event listener.
	 * 
	 * @param listener
	 *          listener.
	 */
	void removeEventListener(EventListener listener);

	/**
	 * Removes all event listeners.
	 */
	void clearEventListeners();

	/**
	 * The event listener for providing callbacks <code>onGoto(Widget page)</code>,
	 * <code>onSubmit</code> and <code>onCancel</code>.
	 * <code>onGoto(Widget page)</code> gets called when <code>gotoXXX()</code>
	 * is invoked or current page index is modified due to removing a page.
	 * <code>onSubmit</code> gets called when <code>submit()</code> is
	 * invoked. <code>onCancel</code> gets called when <code>cancel()</code>
	 * is invoked.
	 */
	interface EventListener extends Serializable {
		/**
		 * Callback handling a change of current page.
		 * 
		 * @param page
		 *          the page got activated.
		 */
		void onGoto(Widget page) throws Exception;

		/**
		 * Callback handling a <code>submit()</code> call.
		 */
		void onSubmit() throws Exception;

		/**
		 * Callback handling a <code>cancel()</code> call.
		 */
		void onCancel() throws Exception;
	}
}
