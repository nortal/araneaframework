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
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * Update region filter, supporting updating of HTML page regions. It processes received request
 * in usual way&mdash;but the response will only contain the updated regions.
 * 
 * @author Nikita Salnikov-Tarnovski
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class StandardUpdateRegionFilterWidget extends BaseFilterWidget {
  static private final Logger log = Logger.getLogger(StandardUpdateRegionFilterWidget.class);

  private String characterEncoding = "UTF-8";

  public static final String UPDATE_REGIONS_KEY = "updateRegions";

  public void setCharacterEncoding(String encoding) {
    characterEncoding = encoding;
  }

  protected void render(OutputData output) throws Exception {
    String commaSeparatedRegions = (String) output.getInputData().getGlobalData().get(UPDATE_REGIONS_KEY); 

    if(commaSeparatedRegions == null) {
      super.render(output);
      return;
    }

    if (log.isDebugEnabled())
      log.debug("Received request to update regions = " + commaSeparatedRegions);

    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);
    try {
      // Parse widget and region names
      Map widgets = getUpdateRegionWidgets(commaSeparatedRegions);

      // Render widgets
      Map responseRegions = getResponseRegions(widgets, arUtil, output);

      // Write out response
      HttpOutputData httpOutput = (HttpOutputData) output;
      writeTransactionId(httpOutput);
      writeUpdateRegions(httpOutput, responseRegions);
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

  protected void writeUpdateRegions(HttpOutputData httpOutput, Map responseRegions) throws Exception {
    for (Iterator i = responseRegions.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String region = (String) entry.getKey();
      byte[] content = (byte[]) entry.getValue();
      httpOutput.getWriter().write("dom\n");
      httpOutput.getWriter().write("replace\n");
      httpOutput.getWriter().write(region + "\n");
      httpOutput.getWriter().write(content.length + "\n");
      httpOutput.getWriter().flush();
      httpOutput.getOutputStream().write(content);
      httpOutput.getOutputStream().flush();
    }
  }

  protected Map getUpdateRegionWidgets(String commaSeparatedRegions) {
    String[] fullRegions = StringUtils.split(commaSeparatedRegions, ',');
    Map widgets = new HashMap();
    for (int i = 0; i < fullRegions.length; i++) {
      // Split each region name by ':' - the first part is widget id, the last part is region name
      // Construct a Map: widgetId -> Set: region name
      String[] widgetAndRegion = StringUtils.split(fullRegions[i], ":", 2);
      Set regions = (Set) widgets.get(widgetAndRegion[0]);
      if (regions == null) {
        regions = new HashSet();
        widgets.put(widgetAndRegion[0], regions);
      }
      if (widgetAndRegion.length > 1) {
        regions.add(widgetAndRegion[1]);
      } else {
        regions.add(null);
      }
    }

    removeDuplicateUpdateRegionWidgets(widgets);

    return widgets;
  }

  protected void removeDuplicateUpdateRegionWidgets(Map widgets) {
    String masterWidget = null;
    Set masterRegions = null;
    for (Iterator i = widgets.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();

      if (masterWidget == null) {
        masterWidget = (String) entry.getKey();
        masterRegions = (Set) entry.getValue();
        continue;
      }

      String widget = (String) entry.getKey();
      Set regions = (Set) entry.getValue();
      if (widget.startsWith(masterWidget + ".")) {
        masterRegions.addAll(regions);
        i.remove();
      } else {
        masterWidget = widget;
        masterRegions = regions;
      }
    }
  }

  protected Map getResponseRegions(Map widgets, AtomicResponseHelper arUtil, OutputData output) throws Exception {
    Map responseRegions = new HashMap();
    for (Iterator i = widgets.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String widget = (String) entry.getKey();
      Set regions = (Set) entry.getValue();

      // render a widget
      Message renderMessage = new RenderMessage(new StandardPath(widget), output);
      propagate(renderMessage);
      

      if (regions.contains(null)) {
        // at least one widget or sub-widget is rendered without updateregion comments
        responseRegions.put(widget, arUtil.getData());
      } else {
        // cut out regions by special comments
        String source = new String(arUtil.getData(), characterEncoding);
        for (Iterator j = regions.iterator(); j.hasNext(); ) {
          String region = (String) j.next();
          responseRegions.put(region, getContentById(source, region).getBytes(characterEncoding));
        }
      }
      arUtil.rollback();
    }
    return responseRegions;
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

    return source.substring(startIndex, endIndex + blockEnd.length());
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

