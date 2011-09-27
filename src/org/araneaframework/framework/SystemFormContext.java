
package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Map;

/**
 * Contract interface for interacting with the system form - the root form that contains data to be sent back to Aranea
 * component hierarchy.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public interface SystemFormContext extends Serializable {

  /**
   * Registers a system form field or updates an existing field with given value.
   * 
   * @param key The name of the field (required not to be empty).
   * @param value Any string value (including <code>null</code>) for the field.
   */
  void addField(String key, String value);

  /**
   * Provides an unmodifiable map of all system form fields with values. If no fields are present, returns an empty map.
   * 
   * @return A map of system form fields with corresponding values.
   */
  Map<String, String> getFields();

}
