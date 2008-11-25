<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.0">

  <ui:widgetContext>
	<script type="text/javascript">
		var widgetId = '<c:out value="${widgetId}" />';

		var pollingUpdater = function(request, response) {
			var text = request.responseText;
			if (text != 'NOTHING') {
				pollAddErrorMessage(text);
			}
		};

		var pollingAction = function() {
			// araneaPage()action(element, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams)
			araneaPage().action($('pollingdiv'), "pollrequest", widgetId, null, pollingUpdater); 
		};

		setInterval(pollingAction, 3000);

		var pollAddErrorMessage = function(msg) {
			var errdiv = $$('div.msg-error').first();
			errdiv.insert(msg);
			errdiv.show();
		};
	</script>
  </ui:widgetContext>
</jsp:root>
