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

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.RoutedMessage;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.AtomicResponseHelper;
import org.araneaframework.http.util.JsonObject;

/**
 * Update region filter, supporting updating of HTML page regions and sending
 * miscellaneous data back via AJAX requests.
 * 
 * @author Nikita Salnikov-Tarnovski
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public class StandardUpdateRegionFilterWidget extends BaseFilterWidget implements UpdateRegionContext {
  static private final Log log = LogFactory.getLog(StandardUpdateRegionFilterWidget.class);

  private String characterEncoding = "UTF-8";
  private Map documentRegions = new HashMap();
  private Map regionHandlers = new HashMap();
  private boolean disabled = false;

  public static final String UPDATE_REGIONS_KEY = "updateRegions";
  public static final String AJAX_REQUEST_ID_KEY = "ajaxRequestId";

  public static final String RELOAD_REGION_KEY = "reload";
  public static final String TRANSACTION_ID_REGION_KEY = "transactionId";
  public static final String DOCUMENT_REGION_KEY = "document";

  public void setCharacterEncoding(String encoding) {
    characterEncoding = encoding;
  }

  public void disableOnce() {
    disabled = true;
  }

  public void addDocumentRegion(String documentRegionId, String widgetId) {
    Assert.notEmptyParam(documentRegionId, "regionName");
    Assert.notEmptyParam(widgetId, "widgetId");
    documentRegions.put(documentRegionId, widgetId);
  }

  public void addRegionHandler(String name, RegionHandler handler) {
    Assert.notEmptyParam(name, "name");
    Assert.notNullParam(handler, "handler");
    regionHandlers.put(name, handler);
  }

  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), UpdateRegionContext.class, this);
  }

  protected void render(OutputData output) throws Exception {
    String regionNames = (String) output.getInputData().getGlobalData().get(UPDATE_REGIONS_KEY); 

    if(regionNames == null) {
      documentRegions.clear();
      super.render(output);
      disabled = false;
      return;
    }

    if (log.isDebugEnabled())
      log.debug("Received request to update regions = " + regionNames);

    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);
    try {
      Map regionContents = null;
      if (!disabled) {
        // Parse widget and region ids
        Map regionIdsByWidgetId = parseRegionNames(regionNames);
        // Render widgets
        regionContents = renderRegions(regionIdsByWidgetId, arUtil, output);
      }

      // Write out response
      HttpOutputData httpOutput = (HttpOutputData) output;
      PrintWriter writer = httpOutput.getWriter();
      String ajaxRequestId = (String) output.getInputData().getGlobalData().get(AJAX_REQUEST_ID_KEY); 
      writeResponseId(writer, ajaxRequestId);
      if (disabled) {
        if (log.isDebugEnabled())
          log.debug("Partial rendering is disabled, forcing a reload for full render");
        writeReloadRegion(writer);
      } else {
        writeTransactionIdRegion(writer);
        writeHandlerRegions(writer);
        writeDocumentRegions(writer, regionContents);
      }
      writer.flush();
    }
    finally {
      arUtil.commit();
      disabled = false;
    }
  }

  protected void writeResponseId(PrintWriter out, String responseId) throws Exception {
    if (responseId != null) {
      out.write(responseId + "\n");
    }
  }

  protected void writeRegion(PrintWriter out, String name, String content) throws Exception {
    out.write(name);
    out.write("\n");
    out.write(Integer.toString(content.length()));
    out.write("\n");
    out.write(content);
  }

  protected void writeReloadRegion(PrintWriter out) throws Exception {
    writeRegion(out, RELOAD_REGION_KEY, "");
  }

  protected void writeTransactionIdRegion(PrintWriter out) throws Exception {
    TransactionContext transactionContext = (TransactionContext) getEnvironment().getEntry(TransactionContext.class);
    if (transactionContext != null) {
      writeRegion(out, TRANSACTION_ID_REGION_KEY, transactionContext.getTransactionId().toString());
    }
  }

  protected void writeHandlerRegions(PrintWriter out) throws Exception {
    for (Iterator i = regionHandlers.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String name = (String) entry.getKey();
      RegionHandler handler = (RegionHandler) entry.getValue();
      String content = handler.getContent();
      if (content != null) {
        writeRegion(out, name, content);
      }
    }
  }

  protected void writeDocumentRegions(PrintWriter out, Map regionContents) throws Exception {
    for (Iterator i = regionContents.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String id = (String) entry.getKey();
      Region region = (Region) entry.getValue();
      JsonObject documentObject = new JsonObject();
      documentObject.setStringProperty("id", id);
      documentObject.setStringProperty("mode", region.getMode());
      StringBuffer buf = new StringBuffer(documentObject.toString());
      buf.insert(0, buf.length() + "\n");
      buf.append(region.getContent());
      writeRegion(out, DOCUMENT_REGION_KEY, buf.toString());
    }
  }

  protected Map parseRegionNames(String commaSeparatedRegionNames) {
    Map regionIdsByWidgetId = new HashMap();

    String[] regionNames = StringUtils.split(commaSeparatedRegionNames, ',');
    for (int i = 0; i < regionNames.length; i++) {
      String documentRegionId = regionNames[i];
      String widgetId = (String) documentRegions.get(documentRegionId);
      if (widgetId == null)
        throw new AraneaRuntimeException("Document region id not found: " + documentRegionId);

      Set regionIds = (Set) regionIdsByWidgetId.get(widgetId);
      if (regionIds == null) {
        regionIds = new HashSet();
        regionIdsByWidgetId.put(widgetId, regionIds);
      }
      regionIds.add(documentRegionId);
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
      if (disabled)  // Our filter was disabled during rendering this widget
        return null; // force page to reload for full render

      // Cut out regions by special comments
      String widgetContent = new String(arUtil.getData(), characterEncoding);
      for (Iterator j = regionIds.iterator(); j.hasNext(); ) {
        String id = (String) j.next();
        regionContents.put(id, new Region(getContentById(widgetContent, id), "update"));
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

  protected static class Region implements Serializable {

    private String content;
    private String mode;

    public Region(String content, String mode) {
      this.content = content;
      this.mode = mode;
    }

    public String getContent() {
      return content;
    }

    public String getMode() {
      return mode;
    }

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