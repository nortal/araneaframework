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

package org.araneaframework.jsp.tag;

/**
 * Interface for tags that output some custom XML(HTML) tags or
 * custom XML(HTML) attributes.
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public interface CustomXMLTagInterface {
  /**
   * Returns name of custom tag that implementing tag should output.
   * @return name of custom tag that implementing tag should output.   
   */
  public String getTagName();

  /**
   * Returns namespace and name of custom tag that implementing tag should output (separated by colon).
   * @return namespace and name of custom tag that implementing tag should output (separated by colon).   
   */
  public String getTagNameWithNS();
}
