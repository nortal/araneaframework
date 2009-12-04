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

package org.araneaframework.uilib.list;

import java.io.Serializable;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * This class incapsulates all the information and behavior connected with the moving through the list pages. That is
 * it translates the moves through the pages and other user activities (like expanding the list) to the exact indexes
 * in of list item range that is to be asked from the database. In addition it provides the renderer with enough
 * information to show the list "sequence", e.g. the breaking of list into pages and blocks.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 *  
 */
public class SequenceHelper implements Serializable {

  /**
   * Default value of how many pages combine into one block for quick navigation.
   */
  public static final long DEFAULT_PAGES_ON_BLOCK = 10;

  /**
   * Default value of how many items of the list will be displayed on one page.
   */
  public static final long DEFAULT_ITEMS_ON_PAGE = 10;
  
  /**
   * Default value of how many items of the list will be displayed on one page.
   */
  public static final long FULL_ITEMS_ON_PAGE = Long.MAX_VALUE;  
  
  /**
   * Whether to preserve the list starting row when switching to showing full list and back.
   */
  public static final boolean DEFAULT_PRESERVE_STARTING_ROW = false;  

  protected ConfigurationContext configuration;
  
  //Whether user has expanded the whole list.
  private boolean allItemsShown = false;

  private long currentPage = 0;
  private long totalItemCount;
  private long itemsOnPage;
  private long pagesOnBlock;
  
  private long firstItemIndex = 0;
  
  private long fullItemsOnPage = FULL_ITEMS_ON_PAGE;
  private long defaultItemsOnPage = DEFAULT_ITEMS_ON_PAGE;
  private long defaultPagesOnBlock = DEFAULT_PAGES_ON_BLOCK;
  private boolean preserveStartingRow = DEFAULT_PRESERVE_STARTING_ROW;
  
  private long oldItemsOnPage;
  private long oldFirstItemIndex;
  
  private boolean changed = true;

  /**
   * Creates the class, setting all parameters to defaults.
   */
  public SequenceHelper(ConfigurationContext configuration) {
    this.configuration = configuration;

    Long confDefaultItemsOnPage = ConfigurationUtil.getDefaultListItemsOnPage(configuration);

    if (confDefaultItemsOnPage != null)
      defaultItemsOnPage = confDefaultItemsOnPage.longValue();

    Long confFullItemsOnPage = (Long) configuration.getEntry(ConfigurationContext.FULL_LIST_ITEMS_ON_PAGE);
    if (confFullItemsOnPage != null)
      fullItemsOnPage = confFullItemsOnPage.longValue();

    Long confDefaultPagesOnBlock = (Long) configuration.getEntry(ConfigurationContext.DEFAULT_LIST_PAGES_ON_BLOCK);
    if (confDefaultPagesOnBlock != null)
      defaultPagesOnBlock = confDefaultPagesOnBlock.longValue();

    Boolean confPreserveStartingRow = (Boolean) configuration.getEntry(ConfigurationContext.LIST_PRESERVE_STARTING_ROW);
    if (confPreserveStartingRow != null)
      preserveStartingRow = confPreserveStartingRow.booleanValue(); 

    setItemsOnPage(defaultItemsOnPage);
    setPagesOnBlock(defaultPagesOnBlock);

    totalItemCount = Long.MAX_VALUE;
  }

  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************

  /**
   * Sets how many items will be displayed on one page.
   */
  public void setItemsOnPage(long itemsOnPage) {
    this.itemsOnPage = itemsOnPage;
    fireChange();
  }
  
  /**
   * Returns how many items will be displayed on one page.
   */
  public long getItemsOnPage() {
  	return this.itemsOnPage;
  }

  /**
   * Sets size of the block.
   * 
   * @param pagesOnBlock
   * number of pages combined into one block.
   */
  public void setPagesOnBlock(long pagesOnBlock) {
    this.pagesOnBlock = pagesOnBlock;
    fireChange();
  }

  /**
   * Sets the page which will be displayed. Page index is 0-based.
   * 
   * @param currentPage
   * index of the page.
   */
  public void setCurrentPage(long currentPage) {
  	if (currentPage < 0)
  		this.currentPage = 0;
  	else if (getPageCount() > 0 && currentPage >= getPageCount())
  		this.currentPage = getPageCount() - 1;
  	else
  		this.currentPage = currentPage;
    
    firstItemIndex = this.currentPage * itemsOnPage;
    
    fireChange();
  }

  /**
   * Sets how many items are there in the list.
   * 
   * @param totalItemCount
   * size of the list.
   */
  public void setTotalItemCount(long totalItemCount) {
    this.totalItemCount = totalItemCount;
  }
  
  /**
   * Gets first item to be displayed on the current page.
   * 
   * @return index of the first element from the list to be displayed.
   */
  public long getCurrentPageFirstItemIndex() {
    return firstItemIndex;
  }

  /**
   * Gets last item to be displayed on the current page.
   * 
   * @return index of the last element from the list to be displayed.
   */
  public long getCurrentPageLastItemIndex() {
  	long result = firstItemIndex + itemsOnPage - 1;
  	if (result >= totalItemCount)
  		result = totalItemCount - 1;
    return result;
  }

  /**
   * Alters indexes to change page to the next one.
   */
  public void goToNextPage() {
  	setCurrentPage(currentPage + 1);
  }

  /**
   * Alters indexes to change page to the pervious one.
   */
  public void goToPreviousPage() {
  	setCurrentPage(currentPage - 1);
  }

  /**
   * Alters indexes to change block to the next one.
   */
  public void goToNextBlock() {
    setCurrentPage(currentPage + pagesOnBlock);
  }

  /**
   * Alters indexes to change block to the previous one.
   */
  public void goToPreviousBlock() {
  	setCurrentPage(currentPage - pagesOnBlock);
  }

  /**
   * Alters indexes to change page to the first one. Page index is 0-based.
   */
  public void goToFirstPage() {
  	setCurrentPage(0);
  }

  /**
   * Alters indexes to change page to the last one.
   */
  public void goToLastPage() {
  	setCurrentPage(getPageCount() - 1);
  }

  /**
   * Alters indexes to change page.
   * 
   * @param page
   * 0-based index of the page to shift to.
   */
  public void goToPage(long page) {
    setCurrentPage(page);
  }
  
  public void validateSequence() {
  	if (currentPage >= getPageCount())
  		goToLastPage();
  }

  /**
   * Expands the list showing all items.
   */
  public void showFullPages() {
    oldItemsOnPage = getItemsOnPage();
    oldFirstItemIndex = firstItemIndex;
  	
  	setItemsOnPage(fullItemsOnPage);
    setCurrentPage((long) Math.ceil((float) firstItemIndex / (float) fullItemsOnPage));
    
    setAllItemsShown(true);
    
    if (preserveStartingRow)
      firstItemIndex = oldFirstItemIndex;
  }

  /**
   * Collapses the list, showing only the current page.
   */
  public void showDefaultPages() {
  	long curFirstItemIndex = preserveStartingRow ? firstItemIndex : oldFirstItemIndex;
  	
  	setItemsOnPage(oldItemsOnPage);
  	setCurrentPage((long) Math.ceil((float) curFirstItemIndex / (float) itemsOnPage));  	
    
    setAllItemsShown(false);
    
    firstItemIndex = curFirstItemIndex;
  }
  
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************

  protected void setAllItemsShown(boolean allItemsShown) {
  	this.allItemsShown = allItemsShown;
  	fireChange();
  }
  
  /** 
   * @since 1.1
   */
  protected boolean getAllItemsShown() {
    return this.allItemsShown;
  }

  /**
   * Gets how many pages are there in the list.
   * 
   * @return number of pages.
   */
  protected long getPageCount() {
  	return (long) Math.ceil((double) this.totalItemCount / (double) this.itemsOnPage);
  }

  /**
   * Gets the first page of the current block. Page index is 0-based.
   * 
   * @return index of the page.
   */
  protected long getBlockFirstPage() {
    long start = 0;
    long end = 0;

    long tempPagesOnBlock = pagesOnBlock;

    if (tempPagesOnBlock > getPageCount()) {
      tempPagesOnBlock = getPageCount();
    }
    long toStartCnt = tempPagesOnBlock / 2;
    long toEndCnt = tempPagesOnBlock - 1 - toStartCnt;

    start = currentPage - toStartCnt;
    end = currentPage + toEndCnt;
    if (start < 0) {
      end += (0 - start);
      start = 0;
    }
    if (end >= getPageCount()) {
      start -= end - getPageCount() + 1;
      end = getPageCount() - 1;
    }

    return start;
  }

  /**
   * Gets the last page of the current block. Page index is 0-based.
   * 
   * @return index of the page.
   */
  protected long getBlockLastPage() {
    long start = 0;
    long end = 0;

    long tempPagesOnBlock = pagesOnBlock;

    if (tempPagesOnBlock > getPageCount()) {
      tempPagesOnBlock = getPageCount();
    }
    long toStartCnt = tempPagesOnBlock / 2;
    long toEndCnt = tempPagesOnBlock - 1 - toStartCnt;

    start = currentPage - toStartCnt;
    end = currentPage + toEndCnt;
    if (start <= 0) {
      end += (0 - start);
      start = 0;
    }
    if (end >= getPageCount()) {
      start -= end - getPageCount() + 1;
      end = getPageCount() - 1;
    }

    return end;
  }
  
  
  /**
   * Returns whether the basic configuration that specifies which items are
   * shown has changed since last call to this {@link SequenceHelper}'s {@link SequenceHelper#checkChanged()} 
   * method.
   * 
   * @since 1.1
   */
  public boolean checkChanged() {
	  boolean result = changed;
	  changed = false;
	  return result;
  }

  //*******************************************************************
  // VIEW MODEL
  //*******************************************************************
  
  /**
   * Returns view model.
   * 
   * @return view model.
   */
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * View Model.
   * 
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
   */
  public class ViewModel implements Serializable {
    private Long firstPage;
    private Long lastPage;
    private Long blockFirstPage;
    private Long blockLastPage;
    private Long currentPage;
    private Long totalItemCount;
    private Boolean allItemsShown;
    private Long pageFirstItem;
    private Long pageLastItem;
    private Long itemsOnPage;

    /**
     * Takes a snapshot of outer class state.
     */
    protected ViewModel() {
      this.firstPage = new Long(0);
      long pageCount = SequenceHelper.this.getPageCount();
      this.lastPage = new Long(pageCount > 0 ? pageCount - 1: pageCount);
      
      this.blockFirstPage = new Long(SequenceHelper.this.getBlockFirstPage());
      this.blockLastPage = new Long(SequenceHelper.this.getBlockLastPage());
      
      this.currentPage = new Long(SequenceHelper.this.currentPage);
      this.totalItemCount = new Long(SequenceHelper.this.totalItemCount);
      
      this.allItemsShown = SequenceHelper.this.allItemsShown ? Boolean.TRUE : Boolean.FALSE;
      
      this.pageFirstItem = new Long(SequenceHelper.this.getCurrentPageFirstItemIndex() + 1);
      this.pageLastItem = new Long(SequenceHelper.this.getCurrentPageLastItemIndex() + 1);
      
      this.itemsOnPage = new Long(SequenceHelper.this.itemsOnPage);
    }

    /**
     * @return Returns the allItemsShown.
     */
    public Boolean getAllItemsShown() {
      return allItemsShown;
    }

    /**
     * @return Returns the blockFirstPage.
     */
    public Long getBlockFirstPage() {
      return blockFirstPage;
    }

    /**
     * @return Returns the blockLastPage.
     */
    public Long getBlockLastPage() {
      return blockLastPage;
    }

    /**
     * @return Returns the currentPage.
     */
    public Long getCurrentPage() {
      return currentPage;
    }

    /**
     * @return Returns the firstPage.
     */
    public Long getFirstPage() {
      return firstPage;
    }

    /**
     * @return Returns the lastPage.
     */
    public Long getLastPage() {
      return lastPage;
    }

    /**
     * @return Returns the pageFirstItem.
     */
    public Long getPageFirstItem() {
      return pageFirstItem;
    }

    /**
     * @return Returns the pageLastItem.
     */
    public Long getPageLastItem() {
      return pageLastItem;
    }

    /**
     * @return Returns the totalItemCount.
     */
    public Long getTotalItemCount() {
      return totalItemCount;
    }

	/**
	 * @return Returns the itemsOnPage.
	 */
	public Long getItemsOnPage() {
		return itemsOnPage;
	}
  }
  
  /** 
   * @since 1.1
   */
  protected void fireChange() {
    changed = true;
  }
}
