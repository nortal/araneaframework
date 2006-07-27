package org.araneaframework.core;

import org.araneaframework.Component;
import org.araneaframework.Message;

public class MessageSeries implements Message {
  private Message[] series;
    
  public MessageSeries(Message[] series) {
    this.series = series;
  }

  public void send(Object id, Component component) {
    for (int i = 0; i < series.length; i++)
      series[i].send(id, component);
  }
}
