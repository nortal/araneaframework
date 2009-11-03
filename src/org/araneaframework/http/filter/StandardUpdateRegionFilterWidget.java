/*
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
 */
package org.araneaframework.http.filter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
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

  private static final Log LOG = LogFactory.getLog(StandardUpdateRegionFilterWidget.class);

  public static final String AJAX_REQUEST_ID_KEY = "ajaxRequestId";
  public static final String RELOAD_REGION_KEY = "reload";
  public static final String TRANSACTION_ID_REGION_KEY = "transactionId";
  public static final String DOCUMENT_REGION_KEY = "document";

  private String characterEncoding = "UTF-8";

  private Map<String, String> documentRegions = new HashMap<String, String>();

  private List<String> renderedRegions = new ArrayList<String>();

  private boolean disabled = false;

  public void setCharacterEncoding(String encoding) {
    this.characterEncoding = encoding;
  }

  public void disableOnce() {
    this.disabled = true;
  }

  public void addDocumentRegion(String documentRegionId, String widgetId) {
    Assert.notEmptyParam(documentRegionId, "regionName");
    Assert.notEmptyParam(widgetId, "widgetId");
    this.documentRegions.put(documentRegionId, widgetId);
  }

  public void addRenderedRegion(String documentRegionId) {
    this.renderedRegions.add(documentRegionId);
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), UpdateRegionContext.class, this);
  }

  @Override
  protected void render(OutputData output) throws Exception {
    String regionsFromRequest = output.getInputData().getGlobalData().get(UpdateRegionContext.UPDATE_REGIONS_KEY);

    StringBuffer regionNames = regionsFromRequest != null ? new StringBuffer(regionsFromRequest) : new StringBuffer();

    if (!this.renderedRegions.isEmpty()) {
      for (String region : this.renderedRegions) {
        regionNames.append(",").append(region);
      }
    }

    if(regionNames.length() == 0) {
      this.documentRegions.clear();
      super.render(output);
      this.disabled = false;
      return;
    }

    if (LOG.isDebugEnabled())
      LOG.debug("Received request to update regions '" + regionNames + "'");

    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);
    try {
      Map<String, Region> regionContents = null;
      if (!this.disabled) {
        // Parse widget and region IDs
        Map<String, Set<String>> regionIdsByWidgetId = parseRegionNames(regionNames.toString());

        // Render widgets
        regionContents = renderRegions(regionIdsByWidgetId, arUtil, output);
      }

      // Write out response
      HttpOutputData httpOutput = (HttpOutputData) output;
      PrintWriter writer = httpOutput.getWriter();
      String ajaxRequestId = output.getInputData().getGlobalData().get(AJAX_REQUEST_ID_KEY);
      writeResponseId(writer, ajaxRequestId);
      if (this.disabled) {
        // TODO: This has some problems with versioned states, but must be tackled on client-side.
        if (LOG.isDebugEnabled()) {
          LOG.debug("Partial rendering is disabled, forcing a reload for full render");
        }
        this.disabled = false;
        writeReloadRegion(writer);
      } else {
        writeTransactionIdRegion(writer);
        writeHandlerRegions(writer);
        writeDocumentRegions(writer, regionContents);
      }
      writer.flush();
    } finally {
      arUtil.commit();
      this.disabled = false;
      this.renderedRegions.clear();
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
    TransactionContext transactionContext = getEnvironment().getEntry(TransactionContext.class);
    if (transactionContext != null) {
      writeRegion(out, TRANSACTION_ID_REGION_KEY, transactionContext.getTransactionId().toString());
    }
  }

  protected void writeHandlerRegions(PrintWriter out) throws Exception {
    UpdateRegionGatherMessage regionGatherMessage = new UpdateRegionGatherMessage();
    propagate(regionGatherMessage);
    for (Map.Entry<String, String> entry : regionGatherMessage.getRegions().entrySet()) {
      if(LOG.isDebugEnabled()) {
        LOG.debug("Updating handler region : " + entry.getKey());
      }
      if (entry.getValue() != null) {
        writeRegion(out, entry.getKey(), entry.getValue());
      }
    }
  }

  protected void writeDocumentRegions(PrintWriter out, Map<String, Region> regionContents) throws Exception {
    for (Map.Entry<String, Region> entry : regionContents.entrySet()) {
      Region region = entry.getValue();
      JsonObject documentObject = new JsonObject();
      documentObject.setStringProperty("id", entry.getKey());
      documentObject.setStringProperty("mode", region.getMode());
      StringBuffer buf = new StringBuffer(documentObject.toString());
      buf.insert(0, buf.length() + "\n");
      buf.append(region.getContent());
      writeRegion(out, DOCUMENT_REGION_KEY, buf.toString());
    }
  }

  protected Map<String, Set<String>> parseRegionNames(String commaSeparatedRegionNames) {
    Map<String, Set<String>> regionIdsByWidgetId = new HashMap<String, Set<String>>();
    String[] regionNames = StringUtils.split(commaSeparatedRegionNames, ',');

    for (int i = 0; i < regionNames.length; i++) {
      String documentRegionId = regionNames[i];
      String widgetId = this.documentRegions.get(documentRegionId);
      if (widgetId == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Document region '" + documentRegionId + "' not found");
        }
        continue;
      }

      Set<String> regionIds = regionIdsByWidgetId.get(widgetId);
      if (regionIds == null) {
        regionIds = new HashSet<String>();
        regionIdsByWidgetId.put(widgetId, regionIds);
      }
      regionIds.add(documentRegionId);
    }

    removeOverlappingRegions(regionIdsByWidgetId);

    return regionIdsByWidgetId;
  }

  protected void removeOverlappingRegions(Map<String, Set<String>> regionIdsByWidgetId) {
    String sourceWidgetId = null;
    Set<String> sourceRegionIds = null;
    for (Iterator<Map.Entry<String, Set<String>>> i = regionIdsByWidgetId.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry<String, Set<String>> entry = i.next();

      if (sourceRegionIds == null) {
        sourceWidgetId = entry.getKey();
        sourceRegionIds = entry.getValue();
        continue;
      }

      String widgetId = entry.getKey();
      Set<String> regionIds = entry.getValue();
      if (widgetId.startsWith(sourceWidgetId + ".")) {
        sourceRegionIds.addAll(regionIds);
        i.remove();
      } else {
        sourceWidgetId = widgetId;
        sourceRegionIds = regionIds;
      }
    }
  }

  protected Map<String, Region> renderRegions(Map<String, Set<String>> regionIdsByWidgetId,
      AtomicResponseHelper arUtil, OutputData output) throws Exception {

    Map<String, Region> regionContents = new HashMap<String, Region>();
    for (Map.Entry<String, Set<String>> entry : regionIdsByWidgetId.entrySet()) {
      String widgetId = entry.getKey();
      Set<String> regionIds = entry.getValue();

      if (LOG.isDebugEnabled()) {
        LOG.debug("Rendering widget '" + widgetId + "'");
      }

      // send a message to identify the component to be rendered
      ComponentLocatorMessage componentLocatorMessage = new ComponentLocatorMessage(new StandardPath(widgetId));
      propagate(componentLocatorMessage);
      if (componentLocatorMessage.getComponent() == null) {
        if (LOG.isWarnEnabled())
          LOG.warn("Widget '" + widgetId + "' not found, skipping rendering");
        continue;
      }

      // send a message to renderable component that resets the render state of Renderable components
      NotRenderedMessage.INSTANCE.send(null, componentLocatorMessage.getComponent());

      // send a message that renders the identified component
      propagate(new RenderMessage(new StandardPath(widgetId), output));

      if (disabled)  // Our filter was disabled during rendering this widget
        return null; // force page to reload for full render

      // Cut out regions by special comments
      String widgetContent = new String(arUtil.getData(), characterEncoding);
      for (String id : regionIds) {
        String content = getContentById(widgetContent, id);
        if (content == null) {
          if (LOG.isWarnEnabled()) {
            LOG.warn("Document region '" + id + "' not found on rendering of widget '" + widgetId + "'");
          }
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

    if (LOG.isDebugEnabled())
      LOG.debug("Successfully extracted region '" + id + "' to be included in response.");

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

    @Override
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

    @Override
    protected void execute(Component component) throws Exception {
      ((Widget) component)._getWidget().render(output);
      ((HttpOutputData) output).getWriter().flush();
    }

  }

  public static class UpdateRegionGatherMessage extends BroadcastMessage {

    private Map<String, String> regions = new HashMap<String, String>();

    @Override
    protected void execute(Component component) throws Exception {
      if (component instanceof UpdateRegionProvider) {
        Map<String, String> newRegions = ((UpdateRegionProvider) component).getRegions(null);
        if (newRegions != null && !newRegions.isEmpty()) {
          if (LOG.isWarnEnabled()) {
            Set<String> duplicateRegions = new HashSet<String>(newRegions.keySet());
            duplicateRegions.retainAll(regions.keySet());
            if (duplicateRegions.size() > 0) {
              LOG.warn("UpdateRegionProvider '" + (component.getScope() != null ? component.getScope().toString() : component.getClass().getName()) + "' overwrites previously added regions: " + duplicateRegions.toString());
            }
          }
          regions.putAll(newRegions);
        }
      }
    }

    public Map<String, String> getRegions() {
      return regions;
    }
  }

}
