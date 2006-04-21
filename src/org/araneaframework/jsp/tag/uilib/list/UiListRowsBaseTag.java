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

package org.araneaframework.jsp.tag.uilib.list;				

import java.io.Writer;
import java.util.ListIterator;
import org.araneaframework.jsp.tag.UiIterationBaseTag;


/**
 * List widget rows tag.
 * 
 * @author Oleg Mürk
 */
public abstract class UiListRowsBaseTag extends UiIterationBaseTag {
	public static final String ROW_REQUEST_ID_KEY_REQUEST = "rowRequestId";
	public static final String ROW_KEY_REQUEST = "org.araneaframework.jsp.ui.uilib.list.UiListRowsTag.ROW";
	
	protected Object currentRow;
	protected ListIterator rowIterator;
	
	protected void init() {
		super.init();
	}
	
	//
	// Implementation
	//
	
	protected void doForEachRow(Writer out) throws Exception {
		pushContextEntry(ROW_KEY_REQUEST, currentRow);
		pushContextEntry(ROW_REQUEST_ID_KEY_REQUEST, Integer.toString(rowIterator.previousIndex()));
	}
	
	protected abstract ListIterator getIterator();
	
	public int before(Writer out) throws Exception {
		super.before(out);
		
		// Get list row iterator 
		rowIterator = getIterator();
		
		// Get first row & continue if needed
		if (rowIterator.hasNext()) {
			currentRow = rowIterator.next();
			
			doForEachRow(out);
			
			return EVAL_BODY_INCLUDE;
		}
		else
			return SKIP_BODY;
	}
	
	protected int afterBody(Writer out) throws Exception {
		// Get next row & continue if needed    
		if (rowIterator.hasNext()) {
			currentRow = rowIterator.next();
			
			doForEachRow(out);
			
			return EVAL_BODY_AGAIN;
		}
		else
			return SKIP_BODY;         
	}
}
