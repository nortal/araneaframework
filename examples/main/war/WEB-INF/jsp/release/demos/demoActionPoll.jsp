<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jstl/core"
  xmlns:fmt="http://java.sun.com/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard"
  xmlns:tui="http://araneaframework.org/tag-library/template"
  version="1.2">

  <ui:widgetContext>
	<script type="text/javascript">
		var pollingUpdater = function(request, response) {

			// If the region does not exist, quit:
			if ($$('div.msg-error').length == 0) {
				clearInterval(intervalActionId);
			}

			var text = request.responseText;
			if (text != 'NOTHING') {
				$$('div.msg-error').first().insert(text).show();
			}
		};

		var pollingAction = function() {
			// Params:   action(element, actionId, actionTarget, actionParam, actionCallback[, options, sync, extraParams])
			araneaPage().action(null, "pollrequest", '<c:out value="${widgetId}" />', null, pollingUpdater);
		};

		var intervalActionId = setInterval(pollingAction, 3000);
	</script>
  </ui:widgetContext>
</jsp:root>
