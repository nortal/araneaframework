package org.araneaframework.integration.gwt;

import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;

/**
 * GwtWidget represents one GWT module.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class GwtWidget extends BaseApplicationWidget {

  private static final long serialVersionUID = 1L;

  public static final String FIRSTRENDER_KEY = "firstRender";

  private String module;

  public GwtWidget(String module) {
    Assert.notEmptyParam(module, "module");
    this.module = module;
  }

  protected void init() throws Exception {
    super.init();
    putViewDataOnce(FIRSTRENDER_KEY, Boolean.TRUE);
  }

  public String getModule() {
    return module;
  }

  public Object getViewModel() throws Exception {
    return new ViewModel();
  }

  public class ViewModel extends BaseApplicationWidget.ViewModel {

    private static final long serialVersionUID = 1L;

    private String module;

    protected ViewModel() throws Exception {      
      this.module = GwtWidget.this.getModule();
    }

    public String getModule() {
      return module;
    }

  }

}
