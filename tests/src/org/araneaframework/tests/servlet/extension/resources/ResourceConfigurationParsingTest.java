
package org.araneaframework.tests.servlet.extension.resources;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.araneaframework.http.extension.ExternalResource;
import org.araneaframework.http.extension.ExternalResourceConfigurationHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class ResourceConfigurationParsingTest extends TestCase {

  private ExternalResource struct;

  private static final List<String> availableGroups = new ArrayList<String>();
  static {
    availableGroups.add("common-styles");
    availableGroups.add("common-js");
  }

  private static final List<String> availableFilesInGroup = new ArrayList<String>();
  static {
    availableFilesInGroup.add("js/mce.js");
    availableFilesInGroup.add("js/calendar.js");
    availableFilesInGroup.add("js/prototype.js");

    availableFilesInGroup.add("js/behaviour.js");
  }

  private static final List<String> allFiles = new ArrayList<String>();
  static {
    allFiles.add("gfx/alfa.gif");
    allFiles.add("gfx/beta.gif");
    allFiles.add("gfx/deta.gif");
    allFiles.add("css/print.css");
    allFiles.add("css/screen.css");
    allFiles.add("css/sexy.css");
    allFiles.add("js/mce_helper.js");
    allFiles.add("js/mce_popup.js");
    allFiles.add("js/mce_debug.js");

    allFiles.addAll(availableFilesInGroup);
  }

  @Override
  public void setUp() throws Exception {
    XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    ExternalResourceConfigurationHandler handler = new ExternalResourceConfigurationHandler();

    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    InputStream stream = FileUtils.openInputStream(new File("./tests/etc/resourcesTest.xml"));
    xr.parse(new InputSource(stream));

    this.struct = handler.getResource();
  }

  public void testGetGroups() throws Exception {
    for (String groupName : this.struct.getGroupNames()) {
      if (!availableGroups.contains(groupName)) {
        fail("Unknown group extracted");
      }
    }
  }

  public void testGetContentsUnion() {
    Map<String, String> map = this.struct.getGroupByName("common-js");

    for (String file : map.keySet()) {

      if (availableFilesInGroup.indexOf(file) == -1) {
        fail("Unknown file in a group");
      }
    }
  }

  public void testAllowedFilesContains() {
    for (String file : allFiles) {
      if (!this.struct.isAllowedFile(file)) {
        fail("File is reported not allowed although in allowed list");
      }
    }
  }

  public void testNotInAllowedFiles() {
    if (this.struct.isAllowedFile("nonexistent")) {
      fail("Non existent file is reported to be allowed");
    }
  }
}
