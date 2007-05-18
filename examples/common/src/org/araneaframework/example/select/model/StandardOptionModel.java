package org.araneaframework.example.select.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;

public class StandardOptionModel implements OptionModel {
	private Collection options;
	private ValueEncoder valueEncoder;
	private DisplayEncoder displayEncoder;
	
	{
		options = new ArrayList();
	}

	public StandardOptionModel() {
		setValueEncoder(new BasicValueEncoder());
		setDisplayEncoder(new BasicDisplayEncoder());
	}

	public StandardOptionModel(Collection options) {
		this();
		//assert serializable
		this.options.addAll(options);
	}
	
	public boolean addOption(Object option) {
		return options.add(option);
	}

	public boolean addOptions(Collection options) {
		return this.options.addAll(options);
	}
	
	public void setDisplayEncoder(DisplayEncoder displayEncoder) {
		this.displayEncoder = displayEncoder;
	}
	
	public void setValueEncoder(ValueEncoder valueEncoder) {
		this.valueEncoder = valueEncoder;
	}
	
	public void setValueAndDisplayEncoder(ValueAndDisplayEncoder encoder) {
		setDisplayEncoder(encoder);
		setValueEncoder(encoder);
	}

	public void setOptions(Collection options) {
		this.options = options;
	}

	public DisplayEncoder getDisplayEncoder() {
		return displayEncoder;
	}

	public ValueEncoder getValueEncoder() {
		return valueEncoder;
	}

	public boolean removeOption(Object option) {
		return options.remove(option);
	}

	public boolean removeOptions(Collection options) {
		return this.options.removeAll(options);
	}
	
	public Map getOptionModelMap() {
		Map result = new LinkedMap();
		for (Iterator i = options.iterator(); i.hasNext();) {
			Object object = i.next();
			result.put(getValueEncoder().getValue(object), object);
		}
		
		return result;
	}

	protected static class BasicDisplayEncoder implements DisplayEncoder {
		private static final long serialVersionUID = 1L;

		public String getDisplay(Object o) {
			return o.toString();
		}
	}
	
	protected static class BasicValueEncoder implements ValueEncoder {
		private static final long serialVersionUID = 1L;

		public String getValue(Object o) {
			return o.toString();
		}
	}
}
