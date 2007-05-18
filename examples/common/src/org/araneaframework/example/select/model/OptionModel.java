package org.araneaframework.example.select.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface OptionModel extends Serializable {
	boolean addOption(Object option);
	boolean removeOption(Object option);
	
	boolean addOptions(Collection options);
	boolean removeOptions(Collection options);
	
	Map getOptionModelMap();
	
	void setValueEncoder(ValueEncoder encoder);
	void setDisplayEncoder(DisplayEncoder encoder);
	void setValueAndDisplayEncoder(ValueAndDisplayEncoder encoder);

	ValueEncoder getValueEncoder();
	DisplayEncoder getDisplayEncoder();
	
	interface ValueEncoder extends Serializable {
		String getValue(Object o);
	}

	interface DisplayEncoder extends Serializable {
		String getDisplay(Object o); 
	}

	interface ValueAndDisplayEncoder extends ValueEncoder, DisplayEncoder {}
}
