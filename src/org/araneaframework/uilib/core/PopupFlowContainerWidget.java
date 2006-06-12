package org.araneaframework.uilib.core;

import org.araneaframework.framework.container.StandardFlowContainerWidget;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class PopupFlowContainerWidget extends StandardFlowContainerWidget {
  PopupFlowPseudoWidget pseudoWidget;

  public PopupFlowContainerWidget(PopupFlowPseudoWidget pseudoWidget) {
    super(pseudoWidget.getWidget());
    this.pseudoWidget = pseudoWidget; 
  }
}
