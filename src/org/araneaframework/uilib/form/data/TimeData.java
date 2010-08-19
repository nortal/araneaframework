package org.araneaframework.uilib.form.data;

import java.text.SimpleDateFormat;
import org.araneaframework.uilib.form.Data;

/**
 * Date data is overwritten here to provide better <code>isStateChanged</code> method for time input form fields.
 * 
 * @author Maksim Boiko (max@webmedia.ee)
 */
public class TimeData extends org.araneaframework.uilib.form.data.DateData {

  /**
   * The problem with different types is solved here (<code>java.util.Date</code> and <code>java.sql.Timestamp</code>).
   * Time is controlled in milliseconds not using <code>equals</code> method as in standard implementation.
   * 
   * @see {@link Data#isStateChanged()}
   */
  @Override
  public boolean isStateChanged() {
    if (markedBaseValue == null && value == null)
      return false;
    else if (markedBaseValue == null || value == null)
      return true;
    else {
      SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
      String formattedMarkedValue = dateFormatter.format(markedBaseValue);
      String formattedValue = dateFormatter.format(value);

      return !formattedMarkedValue.equals(formattedValue);
    }
  }

}
