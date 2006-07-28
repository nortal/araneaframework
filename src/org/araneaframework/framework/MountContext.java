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

package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;

/**
 * This context allows to <i>mount</i> specific pathes, so that when user requests an URL matching this path it would 
 * show him predefined use case. The context uses a message factory instead of any concrete component factory, so that
 * arbitrary actions could be done on the underlying component hierarchy.
 * <p> 
 * A typical application of this context would be to generate an URL including the mounted path and pass it to another user. 
 * The other user may then access the particular use case by simply copying the URL into his browser. Note that the URL may be longer
 * than the mounted path, in that case the suffix will be passed to the message factory and may be used as a parameter.
 * <p>
 * As an example consider that we want to mount client use case to the path <code>/mount/clients</code>.
 * If we now submit an URL <code>http://server/main/mount/clients/3331</code> then 
 * the <code>3331</code> will be passed as the suffix to the message factory and may be used to show the particular client. 
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface MountContext extends Serializable {
  /**
   * Mounts a message factory to the specified URL path prefix. All requests to pathes matching this prefix 
   * will cause the {@link MessageFactory} to be called and the built {@link Message} to be sent.
   * <p>
   * In case several prefixes match the current path the most specific one will be used.
   * 
   * @param pathPrefix The prefix of the path that will be matched aginst the current URL.
   * @param messageFactory The factory that should produce the message used to 
   * 
   * @return The assembled URL pointing to the mounted path prefix.
   */
  public String mount(InputData input, String pathPrefix, MessageFactory messageFactory);
  
  public void unmount(String pathPrefix);
  public String getMountURL(InputData input, String pathPrefix);
  
  public Message getMountedMessage(InputData input);  

  public interface MessageFactory {
    /**
     * Creates a {@link Message} used to update the current component hierarchy in response to the user accessing this specific URL.
     * A message can for example select a menu item, start a flow or authenticate the user. After the message is applied the framework 
     * will proceed to render the result.
     * 
     * @param url The full URL accessed by the user.
     * @param suffix The suffix after the matched mounted path part.
     * @param input Submitted data.
     * @param output Response data.
     * @return Message that will be applied to the component hierarchy.
     */
    public Message buildMessage(String url, String suffix, InputData input, OutputData output);
  }
}
