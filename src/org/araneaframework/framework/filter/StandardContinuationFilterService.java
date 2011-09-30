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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.framework.ContinuationContext;
import org.araneaframework.framework.ContinuationManagerContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * Filter service implementing continuation support. This service also exposes {@link ContinuationManagerContext} and
 * {@link ContinuationContext} to child components through environment.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardContinuationFilterService extends BaseFilterService implements ContinuationManagerContext,
    ContinuationContext {

  private static final Log LOG = LogFactory.getLog(StandardContinuationFilterService.class);

  private Service continuation;

  @Override
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), ContinuationManagerContext.class, this);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);

    try {
      if (isRunning()) {
        LOG.debug("Routing action to continuation");
        this.continuation._getService().action(path, input, output);
      } else {
        arUtil.rollback();

        try {
          LOG.debug("Routing action to child service");
          getChildService()._getService().action(path, input, output);
        } catch (Exception e) {
          if (this.continuation == null) {
            throw e;
          }
          arUtil.rollback();
          LOG.debug("Routing action to continuation");
          this.continuation._getService().action(null, input, output);
        }
      }
    } finally {
      arUtil.commit();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void start(Service continuation) {
    this.continuation = continuation;
    continuation._getComponent().init(getScope(),
        new StandardEnvironment(getEnvironment(), ContinuationContext.class, this));
    throw new AraneaRuntimeException("Continuation set!");
  }

  /**
   * {@inheritDoc}
   */
  public void finish() {
    this.continuation._getComponent().destroy();
    this.continuation = null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRunning() {
    return this.continuation != null;
  }

  /**
   * {@inheritDoc}
   */
  public void runOnce(Service continuation) {
    BaseFilterService service = new BaseFilterService(continuation) {

      @Override
      protected void action(Path path, InputData input, OutputData output) {
        getChildService()._getService().action(path, input, output);
        getEnvironment().getEntry(ContinuationContext.class).finish();
      }
    };
    start(service);
  }
}
