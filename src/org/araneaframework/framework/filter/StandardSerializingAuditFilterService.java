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

package org.araneaframework.framework.filter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Service;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.util.ReadWriteLock;
import org.araneaframework.core.util.ReaderPreferenceReadWriteLock;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * Serializes the the session during the request routing. This filter helps to be aware of serializing issues during
 * development. If the session does not serialize, exception is thrown. <br>
 * <br>
 * The serialized session can be output to a file by setting the xml session path. The path must be valid & writable.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardSerializingAuditFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardSerializingAuditFilterService.class);

  private String testXmlSessionPath;

  private ReadWriteLock callRWLock = new ReaderPreferenceReadWriteLock();

  @Override
  public void setChildService(Service child) {
    this.childService = new RelocatableDecorator(child);
  }

  /**
   * Sets the path where to write the serialized class in xml format. The path must be valid and writable. Example:
   * "/home/user/tmp".
   * 
   * @param testXmlSessionPath
   */
  public void setTestXmlSessionPath(String testXmlSessionPath) {
    this.testXmlSessionPath = testXmlSessionPath;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    this.callRWLock.readLock().acquire();

    try {
      getRelocatable()._getRelocatable().overrideEnvironment(getChildEnvironment());
      super.action(path, input, output);
    } finally {
      this.callRWLock.readLock().release();
    }

    if (this.callRWLock.writeLock().attempt(0)) {
      try {
        getRelocatable()._getRelocatable().overrideEnvironment(null);
        HttpSession sess = getEnvironment().getEntry(HttpSession.class);
        byte[] serialized = SerializationUtils.serialize(this.childService);

        LOG.debug("Serialized session size: " + serialized.length);

        if (this.testXmlSessionPath != null) {
          String dumpPath = this.testXmlSessionPath + "/" + sess.getId() + ".xml";

          LOG.debug("Dumping session XML to '" + dumpPath + "'");

          XStream xstream = new XStream(new DomDriver());
          PrintWriter writer = new PrintWriter(new FileWriter(dumpPath));
          xstream.toXML(this.childService, writer);
          writer.close();
        }
      } finally {
        this.callRWLock.writeLock().release();
      }
    }
  }

  protected Relocatable getRelocatable() {
    return (Relocatable) this.childService;
  }
}
