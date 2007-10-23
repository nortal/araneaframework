package org.araneaframework.example.select.model;

import java.util.Collection;
import org.araneaframework.example.select.model.OptionModel.DisplayEncoder;
import org.araneaframework.example.select.model.OptionModel.ValueEncoder;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SelectControlModel implements SelectModel {
	
	
	public SelectControlModel(Collection c) {
		
	}

	public Object getElementAt(int index) {
		return null;
	}

	public Object getSelectedItem() {
		return null;
	}

	public int getSize() {
		return 0;
	}

	public void setSelectedItem(Object anItem) {
	}

	public void setDisplayEncoder(DisplayEncoder adapter) {
	}

	public void setValueEncoder(ValueEncoder adapter) {
	}
	
	public static class ToStringSelectDisplayAdapter implements DisplayEncoder {
		public String getDisplay(Object object) {
			return object.toString();
		}
	}
	
	public static class ToStringSelectValueAdapter implements ValueEncoder {
		public String encode(Object object) {
			return object.toString();
		}
	}
}
