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

package org.araneaframework.uilib.widgets.lists.tests.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Locale;
import junit.framework.TestCase;
import org.apache.commons.lang.SerializationUtils;
import org.araneaframework.uilib.list.util.comparator.BooleanComparator;
import org.araneaframework.uilib.list.util.comparator.ComparableComparator;
import org.araneaframework.uilib.list.util.comparator.IgnoreCaseComparator;
import org.araneaframework.uilib.list.util.comparator.LocaleStringComparator;
import org.araneaframework.uilib.list.util.comparator.NullComparator;
import org.araneaframework.uilib.list.util.comparator.ReverseComparator;

public class SimpleComparatorTest extends TestCase {
	// UTILITY METHODS
	private NullComparator nullComparator_BooleanComparator_TRUE_FIRST_true() {
		return new NullComparator(BooleanComparator.TRUE_FIRST, true);
	}

	private NullComparator nullComparator_BooleanComparator_FALSE_FIRST_true() {
		return new NullComparator(BooleanComparator.FALSE_FIRST, true);
	}
	
	private LocaleStringComparator localeStringComparator_false_Locale_en() {
		return new LocaleStringComparator(false,  new Locale("en", ""));
	}
	
	// TESTS
	public void testLocaleStringComparatorEqualsTrue_1() {
		LocaleStringComparator first = new LocaleStringComparator(false, Locale.FRENCH);
		LocaleStringComparator second = new LocaleStringComparator(false, Locale.FRENCH);
		
		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testLocaleStringComparatorEqualsTrue_2() {
		LocaleStringComparator first = new LocaleStringComparator(false, Locale.FRENCH);
		LocaleStringComparator second = new LocaleStringComparator(false, new Locale( "fr", ""));

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testLocaleStringComparatorEqualsTrue_3() {
		LocaleStringComparator first = localeStringComparator_false_Locale_en();
		LocaleStringComparator second = localeStringComparator_false_Locale_en();

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}

	public void testLocaleStringComparatorEqualsFalse_1() {
		LocaleStringComparator first = new LocaleStringComparator(true,  new Locale("en", ""));
		LocaleStringComparator second = localeStringComparator_false_Locale_en();

		assertFalse(first.equals(second));
		assertFalse(first.hashCode() == second.hashCode()); // not necessary, but nice
	}
	
	public void testNullComparatorEqualsTrue_1() {
		NullComparator first = new NullComparator(BooleanComparator.FALSE_FIRST, false);
		NullComparator second = new NullComparator(BooleanComparator.FALSE_FIRST, false);
		
		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testNullComparatorEqualsTrue_2() {
		NullComparator first = new NullComparator(localeStringComparator_false_Locale_en(), false);
		NullComparator second = new NullComparator(localeStringComparator_false_Locale_en(), false);
		
		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testNullComparatorEqualsFalse_1() {
		NullComparator first = new NullComparator(localeStringComparator_false_Locale_en(), true);
		NullComparator second = new NullComparator(localeStringComparator_false_Locale_en(), false);
		
		assertFalse(first.equals(second));
		assertFalse(first.hashCode() == second.hashCode());	// not necessary, but nice
	}
	
	public void testNullComparatorEqualsFalse_2() {
		NullComparator first = nullComparator_BooleanComparator_FALSE_FIRST_true();
		NullComparator second = nullComparator_BooleanComparator_TRUE_FIRST_true();
		
		assertFalse(first.equals(second));
		assertFalse(first.hashCode() == second.hashCode());	// not necessary, but nice
	}

	public void testReverseComparatorEqualsTrue_1() {
		ReverseComparator<Boolean> first = new ReverseComparator<Boolean>(BooleanComparator.FALSE_FIRST);
		ReverseComparator<Boolean> second = new ReverseComparator<Boolean>(BooleanComparator.FALSE_FIRST);
		
		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testReverseComparatorEqualsTrue_2() {
		ReverseComparator<String> first = new ReverseComparator<String>(localeStringComparator_false_Locale_en());
		ReverseComparator<String> second = new ReverseComparator<String>(localeStringComparator_false_Locale_en());

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testReverseComparatorEqualsTrue_3() {
		ReverseComparator<Object> first = new ReverseComparator<Object>(nullComparator_BooleanComparator_FALSE_FIRST_true());
		ReverseComparator<Object> second = new ReverseComparator<Object>(nullComparator_BooleanComparator_FALSE_FIRST_true());

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testReverseComparatorEqualsFalse_1() {
		ReverseComparator<Boolean> first = new ReverseComparator<Boolean>(BooleanComparator.FALSE_FIRST);
		ReverseComparator<Boolean> second = new ReverseComparator<Boolean>(BooleanComparator.TRUE_FIRST);

		assertFalse(first.equals(second));
		assertFalse(first.hashCode() == second.hashCode());	// not necessary, but nice
	}
	
	public void testReverseComparatorEqualsFalse_2() {
		ReverseComparator<Object> first = new ReverseComparator<Object>(nullComparator_BooleanComparator_FALSE_FIRST_true());
		ReverseComparator<Object> second = new ReverseComparator<Object>(nullComparator_BooleanComparator_TRUE_FIRST_true());

		assertFalse(first.equals(second));
		assertFalse(first.hashCode() == second.hashCode());	// not necessary, but nice
	}
	
	public void testIgnoreCaseComparatorEqualsTrue_1() {
		IgnoreCaseComparator first = IgnoreCaseComparator.INSTANCE;
		IgnoreCaseComparator second = IgnoreCaseComparator.INSTANCE;

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testIgnoreCaseComparatorEqualsTrue_2() throws IOException, ClassNotFoundException {
		IgnoreCaseComparator first = IgnoreCaseComparator.INSTANCE;
		IgnoreCaseComparator second =
			(IgnoreCaseComparator) new ObjectInputStream(
					new ByteArrayInputStream(SerializationUtils.serialize(first))).readObject();
	    
		assertTrue(first != second);
		
		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testComparableComparatorEqualsTrue_1() {
		ComparableComparator<?> first = ComparableComparator.INSTANCE;
		ComparableComparator<?> second = ComparableComparator.INSTANCE;

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
	
	public void testComparableComparatorEqualsTrue_2() throws IOException, ClassNotFoundException {
		ComparableComparator<?> first = ComparableComparator.INSTANCE;
		ComparableComparator<?> second =
			(ComparableComparator<?>) new ObjectInputStream(
					new ByteArrayInputStream(SerializationUtils.serialize(first))).readObject();
		
		assertTrue(first != second);

		assertTrue(first.equals(second));
		assertTrue(first.hashCode() == second.hashCode());
	}
}
