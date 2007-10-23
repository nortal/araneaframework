package org.araneaframework.example.select.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.bidimap.TreeBidiMap;

public class StandardOptionModel implements OptionModel {
	private Map options;
	private Map optionGroups;
	
	private ValueEncoder valueEncoder;
	private DisplayEncoder displayEncoder;
	
	{
		options = createMap();
	}

	public StandardOptionModel() {
		setValueEncoder(new BasicValueEncoder());
		setDisplayEncoder(new BasicDisplayEncoder());
	}

	public StandardOptionModel(Collection options) {
		this();
		//TODO: assert serializable??
		addOptions(options);
	}
	
	public void addOption(Object option) {
		this.options.put(getValueEncoder().encode(option), option);
	}

	public void addOptions(Collection options) {
		for (Iterator i = options.iterator();i.hasNext();) {
			addOption(i.next());
		}
	}
	
	public boolean hasOption(Object option) {
		Object key = ((TreeBidiMap)options).getKey(option);
		return options.containsValue(option);
	}

	public void addSubOptionGroup(String groupLabel, DisplayEncoder groupLabelEncoder, OptionModel groupOptionModel) {
		if (optionGroups == null)
			optionGroups = createMap();

		optionGroups.put(groupLabel, new StandardOptionGroup(groupLabel, groupLabelEncoder, groupOptionModel));
	}
	
	public void removeSubOptionGroup(String groupLabel) {
		if (optionGroups != null)
			optionGroups.remove(groupLabel);
	}
	
	public Map getOptionGroupMap() {
		return optionGroups != null ? Collections.unmodifiableMap(optionGroups) : Collections.EMPTY_MAP;
	}

	public void setDisplayEncoder(DisplayEncoder displayEncoder) {
		this.displayEncoder = displayEncoder;
	}
	
	public void setValueEncoder(ValueEncoder valueEncoder) {
		this.valueEncoder = valueEncoder;
		
		Map old = options;
		options = createMap();
		addOptions(old.values());
	}

	private Map createMap() {
		return new HashMap();
	}
	
	public void setValueAndDisplayEncoder(ValueAndDisplayEncoder encoder) {
		setDisplayEncoder(encoder);
		setValueEncoder(encoder);
	}

//	public void setOptions(Collection options) {
//		this.options = options;
//	}

	public DisplayEncoder getDisplayEncoder() {
		return displayEncoder;
	}

	public ValueEncoder getValueEncoder() {
		return valueEncoder;
	}

	public boolean removeOption(Object option) {
		return this.options.remove(getValueEncoder().encode(option)) != null;
	}

	public void removeOptions(Collection options) {
		for (Iterator i = options.iterator();i.hasNext();) {
			removeOption(i.next());
		}
	}
	
	public Map getOptionModelMap() {
		return Collections.unmodifiableMap(this.options);
	}
	
	public Object getValue() {
		return null;
	}

	public void setValue(Object option) {
		boolean valueSet = false;
		
		List optionModels = new ArrayList(1+optionGroups.size());
		optionModels.add(this);
		
		for (Iterator i = optionGroups.values().iterator(); i.hasNext(); ) {
			OptionGroup optionGroup = (OptionGroup)i.next();
			optionModels.add(optionGroup.getOptionModel());
		}

		for (Iterator i = optionModels.iterator(); i.hasNext() && !valueSet;) {
			OptionModel model = (OptionModel) i.next();
			if (model.hasOption(option)) {
				valueSet = true;
				model.setValue(option);
			}
		}
		
		if (!valueSet)
			throw new IllegalArgumentException("Given option is not contained by this model.");
	}

	// TODO: visibility?
	protected static class BasicDisplayEncoder implements DisplayEncoder {
		private static final long serialVersionUID = 1L;

		public String getDisplay(Object o) {
			return o.toString();
		}
	}
	
	// TODO: visibility?
	protected static class BasicValueEncoder implements ValueEncoder {
		private static final long serialVersionUID = 1L;

		public String encode(Object o) {
			return String.valueOf(o.hashCode());
		}
	}
	
	// TODO: visibility?
	public static class StandardOptionGroup implements OptionGroup {
		private static final long serialVersionUID = 1L;
		
		private String groupLabel;
		private DisplayEncoder groupLabelEncoder;
		private OptionModel optionModel;
		
		public StandardOptionGroup(String groupLabel, DisplayEncoder groupLabelEncoder, OptionModel optionModel) {
			this.groupLabel = groupLabel;
			this.groupLabelEncoder = groupLabelEncoder;
			this.optionModel = optionModel;
		}

		public String getGroupLabel() {
			return groupLabel;
		}
		public DisplayEncoder getGroupLabelEncoder() {
			return groupLabelEncoder;
		}
		public OptionModel getOptionModel() {
			return optionModel;
		}
	}
}
