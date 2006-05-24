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

package org.araneaframework.jsp.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.updateRegion.UiUpdateRegionCommonTag;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class UiUpdateRegionUtil {
  public static List parseUpdateRegionNames(String updateRegions) throws JspException  {
    return UiUtil.parseMultiValuedAttribute(updateRegions);
  }
  
  public static String getUpdateRegionName(String regionName) {
  	return UiUpdateRegionCommonTag.UPDATE_REGION_PREFIX + regionName;
  }
  
  public static List getUpdateRegionNames(PageContext pageContext, String updateRegions, String globalUpdateRegions) throws JspException {
    List result = UiUpdateRegionUtil.getUpdateRegionLocalNames(pageContext, updateRegions);
    result.addAll(UiUpdateRegionUtil.getUpdateRegionGlobalNames(pageContext, globalUpdateRegions));
    
    return result;
  }
  
  public static List getUpdateRegionLocalNames(PageContext pageContext, String updateRegions) throws JspException  {
    List result = new ArrayList();
    
    String contextWidgetId = UiWidgetUtil.getContextWidgetFullId(pageContext);      
    
    for (Iterator i = parseUpdateRegionNames(updateRegions).iterator(); i.hasNext();) {
      String regionName = (String) i.next();
      
      result.add(UiUpdateRegionCommonTag.UPDATE_REGION_PREFIX + NameUtil.getFullName(contextWidgetId, regionName));
    }
    
    return result;
  }  
  
  public static List getUpdateRegionGlobalNames(PageContext pageContext, String globalUpdateRegions) throws JspException  {
    List result = new ArrayList();
    
    for (Iterator i = parseUpdateRegionNames(globalUpdateRegions).iterator(); i.hasNext();) {
      String regionName = (String) i.next();
      
      result.add(UiUpdateRegionCommonTag.UPDATE_REGION_PREFIX + regionName);
    }
    
    return result;
  }   
  
  public static String formatUpdateRegionsJS(List updateRegions) {
    StringBuffer result = new StringBuffer();
    
    result.append("[");
    
    if (updateRegions != null) {
      for (Iterator i = updateRegions.iterator(); i.hasNext();) {
        String region = (String) i.next();
        
        result.append("'");
        result.append(region);
        result.append("'");
        
        if (i.hasNext())
          result.append(", ");
      }
    }
    
    result.append("]");
    
    return result.toString();
  }
}
