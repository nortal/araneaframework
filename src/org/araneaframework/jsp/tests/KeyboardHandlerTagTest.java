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
import org.araneaframework.jsp.tag.basic.UiKeyboardHandlerBaseTag;

/**
 * @author Konstantin Tretyakov
 */
public class KeyboardHandlerTagTest extends TestCase {

	public KeyboardHandlerTagTest(String name){
		super(name);
	}
	
	public void testKeyToKeyCode(){
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("") == 0);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("a") == 65);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("B") == 66);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("rEtUrn") == 13);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("esCAPE") == 27);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("backSpace") == 8);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("tab") == 9);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("shift") == 16);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("control") == 17);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("sPace") == 32);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode(" ") == 32);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("1") == 49);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("5") == 53);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("somethingelse") == 0);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("f1") == 112);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("f10") == 121);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("F12") == 123);
		assertTrue(UiKeyboardHandlerBaseTag.keyToKeyCode("F13") == 0);
	}
}
