package org.araneaframework.uilib.form.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemContainer;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * Autocomplete with {@link DisplayItem}'s for make benefit the glorious team 
 * of eHL.
 * 
 * What is bad about this is that basically this is nothing more than 
 * {@link org.araneaframework.uilib.form.control.SelectControl}, but as input is
 * text field, there is no good way to restrict user input&mdash;user makes logical
 * assumption that he can enter into the field whatever he wants but is in for
 * a surprise.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class EhlFancyAutoCompleteControl extends AutoCompleteTextControl implements DisplayItemContainer {
	protected boolean actionListeners = true;
	protected LinkedMap itemMap;

	protected void init() throws Exception {
		super.init();
	}
	
	public void setDataProvider(DataProvider dataProvider) {
		assertRemote();
		super.setDataProvider(dataProvider);
	}

	private void _addItem(DisplayItem item) {
		if (itemMap == null)
			itemMap = new LinkedMap();
		itemMap.put(item.getDisplayString(), item);
	}
	
	public void addItem(DisplayItem item) {
		assertLocal();
		_addItem(item);
	}

	public void addItems(Collection items) {
		assertLocal();
		for (Iterator i = items.iterator(); i.hasNext();) 
			_addItem((DisplayItem)i.next());
	}

	public void clearItems() {
		assertLocal();
		itemMap.clear();
	}

	public List getDisplayItems() {
		assertLocal();
		return new ArrayList(itemMap.values());
	}

	public int getValueIndex(String value) {
		assertLocal();
		return DisplayItemUtil.getValueIndex(getDisplayItems(), value);
	}

	protected DisplayItem getValueAsDisplayItem(Object value) {
		assertLocal();
		return (DisplayItem)itemMap.get(value);
	}
	
	public Object getRawValue() {
		if (dataProvider != null)
			return super.getRawValue();
		return getValueAsDisplayItem(super.getRawValue());
	}

	protected void assertLocal() {
		if (actionListeners) {
			clearActionListeners(LISTENER_NAME);
			actionListeners = false;
		}
		if (dataProvider != null)
			throw new IllegalStateException();
	}
	
	protected void assertRemote() {
		if (itemMap != null)
			throw new IllegalStateException();
	}
	

	public String getRawValueType() {
		if (dataProvider != null)
			return super.getRawValueType();
		return "DisplayItem";
	}
	
	public Object getViewModel() {
		return new ViewModel();
	}
	
	public class ViewModel extends AutoCompleteTextControl.ViewModel {
		private List displayItems;
		
		public ViewModel() {
			this.displayItems = EhlFancyAutoCompleteControl.this.getDisplayItems();
		}
		
		public List getDisplayItems() {
			return displayItems;
		}
	}

	// EHL probably wants to override this to ensure that submit really contained
	// value that was sepcified with displayitems if displayitems are used
	// instead dataprovider
	protected void validateNotNull() {
		super.validateNotNull();
	}
}
