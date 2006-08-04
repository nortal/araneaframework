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
import org.araneaframework.jsp.tag.basic.BaseKeyboardHandlerTag;

/**
 * @author Konstantin Tretyakov
 */
public class KeyboardHandlerTagTest extends TestCase {

	public KeyboardHandlerTagTest(String name){
		super(name);
	}
	
	public void testKeyToKeyCode(){
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("") == 0);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("a") == 65);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("B") == 66);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("rEtUrn") == 13);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("esCAPE") == 27);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("backSpace") == 8);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("tab") == 9);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("shift") == 16);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("control") == 17);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("sPace") == 32);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode(" ") == 32);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("1") == 49);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("5") == 53);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("somethingelse") == 0);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("f1") == 112);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("f10") == 121);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("F12") == 123);
		assertTrue(BaseKeyboardHandlerTag.keyToKeyCode("F13") == 0);
	}
}
