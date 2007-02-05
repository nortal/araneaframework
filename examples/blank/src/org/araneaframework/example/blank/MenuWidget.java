package org.araneaframework.example.blank;

import org.araneaframework.OutputData;
import org.araneaframework.uilib.core.BaseMenuWidget;
import org.araneaframework.uilib.core.MenuItem;

public class MenuWidget extends BaseMenuWidget {
	public MenuWidget() throws Exception {
		super(null);
	}

	protected MenuItem buildMenu() throws Exception {
		MenuItem result = new MenuItem();
		result.addMenuItem(new MenuItem("#Blank1", BlankWidget.class));
		result.addMenuItem(new MenuItem("#Blank2", BlankWidget.class));
		result.addMenuItem(new MenuItem("#Blank3", BlankWidget.class));
		result.addMenuItem(new MenuItem("#Blank4", BlankWidget.class));

		return result;
	}

	protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
	}
}
