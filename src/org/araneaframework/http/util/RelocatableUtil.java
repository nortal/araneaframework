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

package org.araneaframework.http.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.SerializationUtils;
import org.araneaframework.Environment;
import org.araneaframework.Relocatable;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.2
 */
public abstract class RelocatableUtil {
  /**
   * Serializes {@link RelocatableService} to byte array.
   */
  public static byte[] serializeRelocatable(RelocatableService service) {
    Assert.notNullParam(service, "service");
    Relocatable.Interface relocatable = service._getRelocatable();
    Environment env = relocatable.getCurrentEnvironment();
    relocatable.overrideEnvironment(null);

    byte[] result = SerializationUtils.serialize(service);
    relocatable.overrideEnvironment(env);

    return result;
  }
  
  /**
   * Serializes {@link RelocatableService} to byte array. If serialization fails,
   * tries serializing it into XML with XStream (http://xstream.codehaus.org/),
   * so that it is possible to debug the issue by looking at partial XML dump. 
   */
  public static byte[] serializeRelocatable(RelocatableService service, String fileName) {
    Assert.notNullParam(service, "service");
    Assert.notNullParam(fileName, "fileName");
    try {
      return serializeRelocatable(service);
    } catch (SerializationException e) {
      try {
        XStream xstream = new XStream(new DomDriver());
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        xstream.toXML(service, writer);
        writer.close();
      } catch (IOException e1) {
        ExceptionUtil.uncheckException(e1);
      }

      throw e;
    }
  }
}
