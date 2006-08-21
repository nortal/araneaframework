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

package org.araneaframework.jsp.tests;

import junit.framework.TestCase;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.jsp.util.JspStringUtil;

/**
 * @author Konstantin Tretyakov
 */
public class StringUtilTest extends TestCase {

	public StringUtilTest(String name){
		super(name);
	}
	
	public void testStringUtils(){
		// underlineAccessKey
		assertEquals(JspStringUtil.underlineAccessKey("",""), "");
		assertEquals(JspStringUtil.underlineAccessKey("aaa",""), "aaa");		
		assertNull(JspStringUtil.underlineAccessKey(null, "a"));
		assertEquals(JspStringUtil.underlineAccessKey("Some string", "So"), "Some string");
		assertEquals(JspStringUtil.underlineAccessKey("Some string", null), "Some string");
		assertEquals(JspStringUtil.underlineAccessKey("Some string", "s"), "<u>S</u>ome string");
		assertEquals(JspStringUtil.underlineAccessKey("s", "s"), "<u>s</u>");
		assertEquals(JspStringUtil.underlineAccessKey(" \n\tõüöä\n\t", "Õ"), " \n\t<u>õ</u>üöä\n\t");
		assertEquals(JspStringUtil.underlineAccessKey("<u>this</u>","U"),"<<u>u</u>>this</u>");
	}
}
