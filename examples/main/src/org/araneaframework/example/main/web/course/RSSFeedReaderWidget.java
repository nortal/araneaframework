package org.araneaframework.example.main.web.course;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.core.Assert;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.FeedMO;
import org.araneaframework.example.main.business.model.UserMO;
import org.araneaframework.framework.MountContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSItem;
import org.gnu.stealthp.rsslib.RSSParser;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class RSSFeedReaderWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private BeanListWidget rssFeedList;
  private FormWidget feedAddForm;
  
  private boolean readOnly;
  private String userName;
  
  private List feeds = new ArrayList();

  public RSSFeedReaderWidget() throws Exception {
  }
  
  /** Constructor for read only view of the subscribed feeds -- for sharing with friends etc*/
  public RSSFeedReaderWidget(boolean readOnly, String userName) {
    this.readOnly = readOnly;
    this.userName = userName;
  }
  
  public void init() throws Exception {
    setViewSelector("course/rssfeedreader");
    
    if (!readOnly) addEventListener("addFeed", new ProxyEventListener(this));
    addEventListener("viewFeedDetails", new ProxyEventListener(this));
    addEventListener("gotoArticle", new ProxyEventListener(this));

    UserMO user = getSecCtx().getUser();

    List oldfeeds = (user != null) ? getFeedDAO().findByUser(user) : getFeedDAO().findByUserName(userName);
    
    for (Iterator feedIterator = oldfeeds.iterator(); feedIterator.hasNext();) {
      feeds.add(parseFeed(((FeedMO)feedIterator.next()).getUrl()));
    }
    
    rssFeedList = createFeedList();
    addWidget("rssFeedList", rssFeedList);
    rssFeedList.setDataProvider(new FileListDataProvider());
    
    if (!readOnly) {
      feedAddForm = createFeedAddForm();
      addWidget("feedAddForm", feedAddForm);
    }
    
    if (!readOnly) {
      putViewData("publicFeedViewURL",  
          ((HttpInputData)getInputData()).getContainerURL() + 
          ((HttpInputData)getInputData()).getPath() + 
          MountContext.MOUNT_PATH + "rssReadOnly?rssSubscriber=" + user.getName());
    }
    putViewData("userName", userName);
  }
  
  private FormWidget createFeedAddForm() {
    FormWidget feedAddForm = new FormWidget();
    feedAddForm.addElement("newFeedUrl", "#Feed URL", new TextControl(), new StringData(), true);
    feedAddForm.getElementByFullName("newFeedUrl").setConstraint(new URLConstraint());
    
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new ProxyOnClickEventListener(this, "addFeed"));
    feedAddForm.addElement("addFeedButton", "#Add Feed", button, null, false);
    return feedAddForm;
  }
  
  private void handleEventAddFeed(String param) throws Exception {
    if (feedAddForm.convertAndValidate()) {
      FeedInfo fi = parseFeed((String)feedAddForm.getValueByFullName("newFeedUrl"));
      FeedMO feed = new FeedMO();
      feed.setUserId(getSecCtx().getUser().getId());
      feed.setUrl(fi.getFeedUrl());

      getFeedDAO().add(feed);

      feeds.add(fi);
      rssFeedList.refresh();
    }
  }
  
  private void handleEventViewFeedDetails(String param) throws Exception {
    FeedInfo fi = (FeedInfo) rssFeedList.getRowFromRequestId(param);
    BeanListWidget feedItemList = new BeanListWidget(RSSItem.class);
    feedItemList.setDataProvider(new FeedItemListDataProvider(fi.getFeedItems()));
    
    RSSItem item = (RSSItem) fi.getFeedItems().iterator().next();
    item.getAuthor();
    
    feedItemList.addField("author", "#Author");
    feedItemList.addField("title", "#Title");
    feedItemList.addField("description", "#Description");
    feedItemList.addField("link", "#Link");
    
    putViewData("detailedFeedTitle", fi.getFeedDescription());
    
    addWidget("feedItemList", feedItemList);
  }
  
  private void handleEventGotoArticle(String param) throws Exception {
    RSSItem item = (RSSItem) ((ListWidget)getWidget("feedItemList")).getRowFromRequestId(param);
    PopupWindowProperties props = new PopupWindowProperties();
    props.setWidth("1000px");
    props.setHeight("800px");
    
    getPopupCtx().open(item.getLink(), props);
  }

  private BeanListWidget createFeedList() throws Exception {
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
      super(FeedInfo.class);
    }

    public List loadData() throws Exception {
      return feeds;
    }
  }
  
  private class FeedItemListDataProvider extends MemoryBasedListDataProvider {
    private static final long serialVersionUID = 1L;
    private List items;

    public FeedItemListDataProvider(List items) {
      super(RSSItem.class);
      this.items = items;
    }

    public List loadData() throws Exception {
      return items;
    }
  }
  
  public static class URLConstraint extends BaseFieldConstraint {
    protected void validateConstraint() throws Exception {
      if (getValue() != null) {
        try {
          URL u = new URL((String)getValue());
        } catch (MalformedURLException e) {
          addError("Field '" + t(getLabel()) + "' does not contain valid URL.");
        }
      }
    }
  }

  public static class FeedInfo implements Serializable {
    private String feedUrl;
    private String feedDescription;
    
    public FeedInfo(String feedUrl, String feedDescription) {
      Assert.notNullParam(feedUrl, "feedUrl");
      
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
    
    List getFeedItems() throws Exception {
      RSSHandler hand = new RSSHandler();
      URL u = new URL(getFeedUrl());
      RSSParser.parseXmlFile(u,hand,false);
      RSSChannel ch = hand.getRSSChannel();
      return ch.getItems();
    }
  }
}
