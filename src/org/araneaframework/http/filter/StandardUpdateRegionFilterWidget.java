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

package org.araneaframework.http.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.RoutedMessage;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * Update region filter, supporting updating of HTML page regions. It processes received request
 * in usual way&mdash;but the response will only contain the updated regions.
 * 
 * @author Nikita Salnikov-Tarnovski
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class StandardUpdateRegionFilterWidget extends BaseFilterWidget implements UpdateRegionContext {
  static private final Logger log = Logger.getLogger(StandardUpdateRegionFilterWidget.class);

  private String characterEncoding = "UTF-8";
  private boolean renderFullPage = false;

  public static final String UPDATE_REGIONS_KEY = "updateRegions";

  public void setCharacterEncoding(String encoding) {
    characterEncoding = encoding;
  }

  public void setRenderFullPage() {
    renderFullPage = true;
  }

  protected void render(OutputData output) throws Exception {
    String regionNames = (String) output.getInputData().getGlobalData().get(UPDATE_REGIONS_KEY); 

    if(regionNames == null) {
      super.render(output);
      return;
    }

    if (log.isDebugEnabled())
      log.debug("Received request to update regions = " + regionNames);

    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);
    try {
      // Parse widget and region ids
      Map regionIdsByWidgetId = parseRegionNames(regionNames);

      // Render widgets
      Map regionContents = renderRegions(regionIdsByWidgetId, arUtil, output);

      // Write out response
      HttpOutputData httpOutput = (HttpOutputData) output;
      writeTransactionId(httpOutput);
      writeRegions(httpOutput, regionContents);
    }
    finally {
      arUtil.commit();
    }
  }

  protected void writeTransactionId(HttpOutputData httpOutput) throws Exception {
    TransactionContext transactionContext = (TransactionContext) getEnvironment().getEntry(TransactionContext.class);
    if (transactionContext != null) {
      httpOutput.getWriter().write("transactionId\n");
      httpOutput.getWriter().write(transactionContext.getTransactionId() + "\n");
      httpOutput.getWriter().flush();
    }
  }

  protected void writeRegions(HttpOutputData httpOutput, Map regionContents) throws Exception {
    for (Iterator i = regionContents.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String id = (String) entry.getKey();
      byte[] content = (byte[]) entry.getValue();
      httpOutput.getWriter().write("dom\n");
      httpOutput.getWriter().write(id + "\n");
      httpOutput.getWriter().write(content.length + "\n");
      httpOutput.getWriter().flush();
      httpOutput.getOutputStream().write(content);
      httpOutput.getOutputStream().flush();
    }
  }

  protected Map parseRegionNames(String commaSeparatedRegionNames) {
    String[] regionNames = StringUtils.split(commaSeparatedRegionNames, ',');
    Map regionIdsByWidgetId = new HashMap();
    for (int i = 0; i < regionNames.length; i++) {
      // Split each region name by ':' - the first part is widget id, the last
      // part is region id. Construct a Map: (widgetId -> Set: (regionId))
      String[] widgetIdAndRegionId = StringUtils.split(regionNames[i], ":", 2);
      Set regionIds = (Set) regionIdsByWidgetId.get(widgetIdAndRegionId[0]);
      if (regionIds == null) {
        regionIds = new HashSet();
        regionIdsByWidgetId.put(widgetIdAndRegionId[0], regionIds);
      }
      if (widgetIdAndRegionId.length > 1) {
        regionIds.add(widgetIdAndRegionId[1]);
      } else {
        // Only widgetId is present - add null value to indicate that this
        // widget has to be fully rendered
        regionIds.add(null);
      }
    }

    removeOverlappingRegions(regionIdsByWidgetId);

    return regionIdsByWidgetId;
  }

  protected void removeOverlappingRegions(Map regionIdsByWidgetId) {
    String sourceWidgetId = null;
    Set sourceRegionIds = null;
    for (Iterator i = regionIdsByWidgetId.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();

      if (sourceRegionIds == null) {
        sourceWidgetId = (String) entry.getKey();
        sourceRegionIds = (Set) entry.getValue();
        continue;
      }

      String widgetId = (String) entry.getKey();
      Set regionIds = (Set) entry.getValue();
      if (widgetId.startsWith(sourceWidgetId + ".")) {
        sourceRegionIds.addAll(regionIds);
        i.remove();
      } else {
        sourceWidgetId = widgetId;
        sourceRegionIds = regionIds;
      }
    }
  }

  protected Map renderRegions(Map regionIdsByWidgetId, AtomicResponseHelper arUtil, OutputData output) throws Exception {
    Map regionContents = new HashMap();
    for (Iterator i = regionIdsByWidgetId.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String widgetId = (String) entry.getKey();
      Set regionIds = (Set) entry.getValue();

      if (log.isDebugEnabled())
        log.debug("Rendering widget " + widgetId);
      Message renderMessage = new RenderMessage(new StandardPath(widgetId), output);
      propagate(renderMessage);

      if (regionIds.contains(null)) {
        // At least one widget has to be fully rendered (without updateregion
        // comments)
        regionContents.put(widgetId, arUtil.getData());
      } else {
        // Cut out regions by special comments
        String widgetContent = new String(arUtil.getData(), characterEncoding);
        for (Iterator j = regionIds.iterator(); j.hasNext(); ) {
          String id = (String) j.next();
          regionContents.put(id, getContentById(widgetContent, id).getBytes(characterEncoding));
        }
      }
      arUtil.rollback();
    }
    return regionContents;
  }

  protected String getContentById(String source, String id) {
    String blockStart = "<!--BEGIN:" + id + "-->";
    int startIndex = source.indexOf(blockStart);

    if(startIndex == -1)
      return "";

    String blockEnd = "<!--END:" + id + "-->";

    int endIndex = source.indexOf(blockEnd);

    if(endIndex == -1)
      throw new IllegalStateException("Expected END block for AJAX update region with id '" + id + "'.");

    if (log.isDebugEnabled())
      log.debug("Successfully extracted region '" + id + "' to be included in response.");

    return source.substring(startIndex + blockStart.length(), endIndex);
  }

  public static class RenderMessage extends RoutedMessage {

    private OutputData output;

    public RenderMessage(Path path, OutputData output) {
      super(path);
      this.output = output;
    }

    protected void execute(Component component) throws Exception {
      ((Widget) component)._getWidget().render(output);
      ((HttpOutputData) output).getWriter().flush();
    }

  }

}
