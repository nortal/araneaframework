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
import org.araneaframework.core.Assert;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.RoutedMessage;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.UpdateRegionProvider;
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
  private boolean disabled = false;

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

  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), UpdateRegionContext.class, this);
  }

  protected void render(OutputData output) throws Exception {
    String regionNames = (String) output.getInputData().getGlobalData().get(UpdateRegionContext.UPDATE_REGIONS_KEY); 

    if(regionNames == null) {
      documentRegions.clear();
      super.render(output);
      disabled = false;
      return;
    }

    if (log.isDebugEnabled())
      log.debug("Received request to update regions '" + regionNames + "'");

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
    UpdateRegionGatherMessage regionGatherMessage = new UpdateRegionGatherMessage();
    propagate(regionGatherMessage);
    for (Iterator i = regionGatherMessage.getRegions().entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String name = (String) entry.getKey();
      if(log.isDebugEnabled()) {
        log.debug("Updating handler region : " + name);
      }
      String content = (String) entry.getValue();
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
      if (widgetId == null) {
        if (log.isWarnEnabled())
          log.warn("Document region '" + documentRegionId + "' not found");
        continue;
      }

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
        log.debug("Rendering widget '" + widgetId + "'");

      // send a message to identify the component to be rendered 
      ComponentLocatorMessage componentLocatorMessage = new ComponentLocatorMessage(new StandardPath(widgetId));
      propagate(componentLocatorMessage);
      if (componentLocatorMessage.getComponent() == null) {
        if (log.isWarnEnabled())
          log.warn("Widget '" + widgetId + "' not found, skipping rendering");
        continue;
      }

      // send a message to renderable component that resets the render state of Renderable components
      NotRenderedMessage.INSTANCE.send(null, componentLocatorMessage.getComponent());

      // send a message that renders the identified component
      Message renderMessage = new RenderMessage(new StandardPath(widgetId), output);
      propagate(renderMessage);

      if (disabled)  // Our filter was disabled during rendering this widget
        return null; // force page to reload for full render

      // Cut out regions by special comments
      String widgetContent = new String(arUtil.getData(), characterEncoding);
      for (Iterator j = regionIds.iterator(); j.hasNext(); ) {
        String id = (String) j.next();
        String content = getContentById(widgetContent, id);
        if (content == null) {
          if (log.isWarnEnabled())
            log.warn("Document region '" + id + "' not found on rendering of widget '" + widgetId + "'");
          continue;
        }
        regionContents.put(id, new Region(content, "update"));
      }
      arUtil.rollback();
    }
    return regionContents;
  }

  protected String getContentById(String source, String id) {
    String blockStart = "<!--BEGIN:" + id + "-->";
    int startIndex = source.indexOf(blockStart);

    if(startIndex == -1)
      return null;

    String blockEnd = "<!--END:" + id + "-->";

    int endIndex = source.indexOf(blockEnd);

    if(endIndex == -1)
      throw new IllegalStateException("Expected END block for AJAX update region with id '" + id + "'.");

    if (log.isDebugEnabled())
      log.debug("Successfully extracted region '" + id + "' to be included in response.");

    return source.substring(startIndex + blockStart.length(), endIndex);
  }

  public static class Region implements Serializable {

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

  public static class ComponentLocatorMessage extends RoutedMessage {

    private Component component;

    public ComponentLocatorMessage(Path path) {
      super(path);
    }

    protected void execute(Component component) throws Exception {
      this.component = component;
    }

    public Component getComponent() {
      return this.component;
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

  public static class UpdateRegionGatherMessage extends BroadcastMessage {

    private Map regions = new HashMap();

    protected void execute(Component component) throws Exception {
      if (component instanceof UpdateRegionProvider) {
        Map newRegions = ((UpdateRegionProvider) component).getRegions();
        if (newRegions != null && !newRegions.isEmpty()) {
          if (log.isWarnEnabled()) {
            Set duplicateRegions = new HashSet(newRegions.keySet());
            duplicateRegions.retainAll(regions.keySet());
            if (duplicateRegions.size() > 0) {
              log.warn("UpdateRegionProvider '" + (component.getScope() != null ? component.getScope().toString() : component.getClass().getName()) + "' overwrites previously added regions: " + duplicateRegions.toString());
            }
          }
          regions.putAll(newRegions);
        }
      }
    }

    public Map getRegions() {
      return regions;
    }

  }

}
