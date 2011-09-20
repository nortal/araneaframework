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

package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;

/**
 * This context allows to <i>mount</i> an action (a {@link Message}) to specific paths, so that when user requests an
 * URI matching this path it would show/return predefined use case. The context uses a message factory instead of any
 * concrete component factory, so that arbitrary actions could be done on the underlying component hierarchy.
 * <p>
 * A typical application of this context would be to generate an URL including the mounted path and pass it to another
 * user. The other user may then access the particular use case by simply copying the URL into his browser. Note that
 * the URL may be longer than the mounted path, in that case the suffix will be passed to the message factory and may be
 * used as a parameter.
 * <p>
 * As an example consider that we want to mount client use case to the path <code>/mount/clients</code>. If we now
 * submit an URL <code>http://server/main/mount/clients/3331</code> then the <code>3331</code> will be passed as the
 * suffix to the message factory and may be used to show the particular client.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see org.araneaframework.framework.filter.StandardMountPointFilterService
 */
public interface MountContext extends Serializable {

  /**
   * The mount request path prefix.
   */
  String MOUNT_PATH = "/mount/";

  /**
   * Mounts a message factory to the specified URI prefix. All requests to paths matching this prefix will cause the
   * {@link MessageFactory} to be called and the built {@link Message} to be sent.
   * <p>
   * In case several prefixes match the current path the most specific one will be used.
   * 
   * @param input Input data representing the current HTTP request.
   * @param uriPrefix The prefix of the URI that will be matched against the current URL.
   * @param messageFactory The factory that should produce the message used to
   * @return The assembled full URL pointing to the mounted path prefix.
   * @see #getMountURL(InputData, String)
   */
  String mount(InputData input, String uriPrefix, MessageFactory messageFactory);

  /**
   * Unmounts the message factory from the specified URI prefix.
   * 
   * @param uriPrefix Mounted URI prefix.
   */
  void unmount(String uriPrefix);

  /**
   * Returns an assembled full URL pointing to the mounted URI prefix. URL can be further modified by appending the path
   * or query parameters.
   * 
   * @param input Input data representing the current HTTP request.
   * @param uri Mounted URI prefix.
   * @return The assembled full URL pointing to the mounted path prefix.
   */
  String getMountURL(InputData input, String uri);

  /**
   * Returns the {@link Message} that applies the mounted action corresponding to the current URL. Used primarily by the
   * <tt>StandardMountPointFilterService</tt> or similar services to <i>mount</i> the application state corresponding to
   * the URL.
   * 
   * @param input Input data representing the current HTTP request.
   * @return The {@link Message} corresponding to the current mounted URL or <code>null</code>.
   * @see org.araneaframework.framework.filter.StandardMountPointFilterService
   */
  Message getMountedMessage(InputData input);

  /**
   * The factory that creates necessary messages to start the widget.
   */
  interface MessageFactory extends Serializable {

    /**
     * Creates a {@link Message} used to update the current component hierarchy in response to the user accessing this
     * specific URL. A message can for example select a menu item, start a flow or authenticate the user. After the
     * message is applied the framework will proceed to render the result.
     * 
     * @param url The full URL accessed by the user.
     * @param suffix The suffix after the matched mounted path part.
     * @param input Submitted data.
     * @param output Response data.
     * @return Message that will be applied to the component hierarchy.
     */
    Message buildMessage(String url, String suffix, InputData input, OutputData output);
  }

}
