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
import java.io.Writer;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
import org.araneaframework.framework.core.BaseFilterService;

/**
 * Filter service that serializes the child component during the request routing. It helps to be aware of serializing
 * issues during development. If the session does not serialize, exception is thrown.
 * <p>
 * The serialized session can be output to a file by setting the XML session path. The path must be valid and writable.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardSerializingAuditFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardSerializingAuditFilterService.class);

  private static final BigDecimal BYTES_DIVIDER = BigDecimal.valueOf(1024);

  private String testXmlSessionPath;

  private final ReadWriteLock callRWLock = new ReentrantReadWriteLock(true);

  @Override
  public void setChildService(Service child) {
    super.setChildService(new RelocatableDecorator(child));
  }

  /**
   * Sets the path where to write the serialized classes in XML format. The path must be valid and writable. Example:
   * "/home/user/tmp".
   * <p>
   * NOTE: to write serialized classes as XML, <tt>XStream</tt> (<tt>com.thoughtworks.xstream</tt>) must be in
   * class-path.
   * 
   * @param testXmlSessionPath The path where to write the serialized classes in XML format.
   */
  public void setTestXmlSessionPath(String testXmlSessionPath) {
    this.testXmlSessionPath = testXmlSessionPath;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    this.callRWLock.readLock().lock();

    try {
      getRelocatable()._getRelocatable().overrideEnvironment(getChildEnvironment());
      super.action(path, input, output);
    } finally {
      this.callRWLock.readLock().unlock();
    }

    if (this.callRWLock.writeLock().tryLock()) {
      try {
        getRelocatable()._getRelocatable().overrideEnvironment(null);
        HttpSession sess = getEnvironment().getEntry(HttpSession.class);
        byte[] serialized = SerializationUtils.serialize(getChildService());

        if (LOG.isDebugEnabled()) {
          LOG.debug("Serialized session size: " + formatSize(serialized.length));
        }

        if (this.testXmlSessionPath != null) {
          String dumpPath = this.testXmlSessionPath + "/" + sess.getId() + ".xml";

          LOG.debug("Dumping session XML to '" + dumpPath + "'");

          XStream xstream = new XStream(new DomDriver());
          Writer writer = new PrintWriter(new FileWriter(dumpPath));
          xstream.toXML(getChildService(), writer);
          writer.close();
        }
      } finally {
        this.callRWLock.writeLock().unlock();
      }
    }
  }

  /**
   * Returns the child service as relocatable since this service requires that the child service must also be
   * relocatable.
   * 
   * @return The child - a relocatable service.
   */
  protected Relocatable getRelocatable() {
    return (Relocatable) getChildService();
  }

  /**
   * Formats the size (provided in bytes) into human-friendly format using B, kB, MB, GB or TB as a unit.
   * 
   * @param size The size in bytes to format.
   * @return The formatted size with units.
   */
  protected static String formatSize(int size) {
    BigDecimal fSize = BigDecimal.valueOf(size);

    char[] units = { ' ', 'k', 'M', 'G', 'T' };
    int unit = 0;

    while (unit < units.length - 2 && fSize.compareTo(BYTES_DIVIDER) > -1) {
      fSize = fSize.divide(BYTES_DIVIDER);
      unit++;
    }

    // Do some rounding to make the value easier to understand.
    fSize = fSize.setScale(2, BigDecimal.ROUND_HALF_UP);
    return fSize.toString() + units[unit] + 'B';
  }
}
