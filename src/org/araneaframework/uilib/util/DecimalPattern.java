package org.araneaframework.uilib.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * @author <a href="mailto:rein@webmedia.ee>Rein Raudjärv</a>
 */
public class DecimalPattern implements Serializable {
	private String pattern;
	private DecimalFormatSymbols symbols;
	
	public DecimalPattern(String pattern, DecimalFormatSymbols symbols) {
		this.pattern = pattern;
		this.symbols = symbols == null ? null : (DecimalFormatSymbols) symbols.clone();
	}
	
	public String getPattern() {
		return pattern;
	}
	public DecimalFormatSymbols getSymbols() {
		return symbols;
	}
	
	public String toString() {
		return new StringBuffer("Decimal Pattern [").append(pattern).append("]").toString();
	}
	
	public NumberFormat getNumberFormat() {
		NumberFormat result = null;
		
		if (getPattern() == null) {
			result = new DecimalFormat(); 
		} else if (getSymbols() == null) {
			result = new DecimalFormat(getPattern());	
		} else {
			result = new DecimalFormat(getPattern(), getSymbols());
		}
		
		return result;
	}
}
