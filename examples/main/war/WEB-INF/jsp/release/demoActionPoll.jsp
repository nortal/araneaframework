<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard"
  xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.1">

  <ui:widgetContext>
	<script type="text/javascript">
		window.hasExecuted = false;

		var pollingUpdater = function(request, response) {

			// If the region does not exist, quit:
			if (window.hasExecuted &amp;&amp; $$('div.msg-error').first().empty()) {
				clearInterval(intervalActionId);
				window.hasExecuted = null;
			}

			var text = request.responseText;
			if (text != 'NOTHING') {
				$$('div.msg-error').first().insert(text).show();
				window.hasExecuted = true;
			}
		};

		var pollingAction = function() {
			// Params:  action(actionId, actionTarget, actionParam, actionCallback[, options, sync, extraParams])
			Aranea.Page.action("pollrequest", '${widgetId}', null, null, pollingUpdater);
		};

		var intervalActionId = setInterval(pollingAction, 3000);
	</script>
  </ui:widgetContext>
</jsp:root>
