package org.araneaframework.example.select.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * TODO: review return types
 */
public interface OptionModel extends Serializable {
	// flat option management
	void addOption(Object option);
	boolean removeOption(Object option);
	
	void addOptions(Collection options);
	void removeOptions(Collection options);
	
	Map getOptionModelMap();
	
	/** @return whether this {@link OptionModel} contains the option */
	boolean hasOption(Object option);
	
	// option grouping
	void addSubOptionGroup(String groupLabel, DisplayEncoder groupLabelEncoder, OptionModel groupOptionModel);
	void removeSubOptionGroup(String groupLabel);
	
	Map getOptionGroupMap();

	interface OptionGroup extends Serializable {
		String getGroupLabel();
		DisplayEncoder getGroupLabelEncoder();
		OptionModel getOptionModel();
	}

	// value and display encoding
	void setValueEncoder(ValueEncoder encoder);
	void setDisplayEncoder(DisplayEncoder encoder);
	void setValueAndDisplayEncoder(ValueAndDisplayEncoder encoder);

	ValueEncoder getValueEncoder();
	DisplayEncoder getDisplayEncoder();
	
	interface ValueEncoder extends Serializable {
		String encode(Object o);
	}

	interface DisplayEncoder extends Serializable {
		String getDisplay(Object o); 
	}

	interface ValueAndDisplayEncoder extends ValueEncoder, DisplayEncoder {}
	
	// setting and getting the current value, asking whether the value is already present
	void setValue(Object option);
	Object getValue();
}
