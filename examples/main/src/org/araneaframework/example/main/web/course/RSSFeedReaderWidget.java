package org.araneaframework.example.main.web.course;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.support.FileInfo;
import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSParser;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class RSSFeedReaderWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private BeanListWidget rssFeedList;
  private FormWidget feedAddForm;
  
  private List feeds = new ArrayList();

  public RSSFeedReaderWidget() throws Exception {
    FeedInfo f1 = parseFeed("http://blog.araneaframework.org/feed/");
    FeedInfo f2 = parseFeed("http://www.zeroturnaround.com/feed/");
    feeds.add(f1);
    feeds.add(f2);
  }
  
  public void init() throws Exception {
    setViewSelector("course/rssfeedreader");
    rssFeedList = buildList();
    addWidget("rssFeedList", rssFeedList);
    rssFeedList.setDataProvider(new FileListDataProvider());
    feedAddForm = new FormWidget();
    feedAddForm.addElement("newFeedUrl", "#Add new feed", new TextControl(), new StringData(), true);
    feedAddForm.getElementByFullName("newFeedUrl").setConstraint(new URLConstraint());
    
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new ProxyOnClickEventListener(this, "addFeed"));
    feedAddForm.addElement("addFeedButton", "#Add Feed", button, null, false);
    
    addWidget("feedAddForm", feedAddForm);
  }
  
  private void handleEventAddFeed(String param) throws Exception {
    if (feedAddForm.convertAndValidate()) {
      FeedInfo fi = parseFeed((String)feedAddForm.getValueByFullName("newFeedUrl"));
      feeds.add(fi);
      rssFeedList.refresh();
    }
  }

  private BeanListWidget buildList() throws Exception {
    BeanListWidget l = new BeanListWidget(FeedInfo.class);
    l.addField("feedUrl", "#Feed URL");
    l.addField("feedDescription", "#Feed Description");
    return l;
  }
  
  FeedInfo parseFeed(String url) throws Exception {
    RSSHandler hand = new RSSHandler();
    URL u = new URL(url);
    RSSParser.parseXmlFile(u,hand,false);
    RSSChannel ch = hand.getRSSChannel();
    
    ch.getDescription();
    
    return new FeedInfo(url, ch.getDescription());
  }

  // INNER CLASSES
  private class FileListDataProvider extends MemoryBasedListDataProvider {
    private static final long serialVersionUID = 1L;

    public FileListDataProvider() {
      super(FileInfo.class);
    }

    public List loadData() throws Exception {
      return feeds;
    }
  }
  
  public static class URLConstraint extends BaseFieldConstraint {
    protected void validateConstraint() throws Exception {
      if (getValue() != null) {
        try {
          URL u = new URL((String)getValue());
        } catch (MalformedURLException e) {
          addError("Field '" + getLabel() + "' does not contain valid URL.");
        }
      }
    }
  }
  
  public static class FeedInfo implements Serializable {
    private String feedUrl;
    private String feedDescription;
    
    public FeedInfo(String feedUrl, String feedDescription) {
      this.feedUrl = feedUrl;
      this.feedDescription = feedDescription;
    }
    public String getFeedUrl() {
      return feedUrl;
    }
    public void setFeedUrl(String feedUrl) {
      this.feedUrl = feedUrl;
    }
    public String getFeedDescription() {
      return feedDescription;
    }
    public void setFeedDescription(String feedDescription) {
      this.feedDescription = feedDescription;
    }
  }
}
