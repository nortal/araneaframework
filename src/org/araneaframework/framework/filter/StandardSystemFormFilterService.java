package org.araneaframework.framework.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * Stores system form fields that will be written out in &lt;ui:systemForm&gt;
 * tag. This implementation adds <code>topServiceId</code> and
 * <code>threadServiceId</code> automatically, since
 * <code>SystemFormContext</code> is usually located below them in the
 * hierarchy.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public class StandardSystemFormFilterService extends BaseFilterService implements SystemFormContext {
  private Map<String, String> fields = new HashMap<String, String>();

  @Override
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), SystemFormContext.class, this);
  }

  /**
   * Registers the <code>topServiceId</code> and <code>threadServiceId</code>
   * fields from the <code>Envrionment</code>.
   */
  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    fields.clear();

    Object topServiceId = EnvironmentUtil.getTopServiceId(getEnvironment());
    if (topServiceId != null)
      addField(TopServiceContext.TOP_SERVICE_KEY, topServiceId.toString());

    Object threadServiceId = EnvironmentUtil.getThreadServiceId(getEnvironment());
    if (threadServiceId != null)
      addField(ThreadContext.THREAD_SERVICE_KEY, threadServiceId.toString());

    super.action(path, input, output);
  }

  public void addField(String key, String value) {
    Assert.notEmptyParam(key, "key");
    Assert.notNullParam(value, "value");
    fields.put(key, value);
  }

  public Map<String, String> getFields() {
    return Collections.unmodifiableMap(fields);
  }
}
