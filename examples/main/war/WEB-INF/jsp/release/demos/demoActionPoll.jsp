<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jstl/core"
  xmlns:fmt="http://java.sun.com/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard"
  xmlns:tui="http://araneaframework.org/tag-library/template"
  version="1.2">

  <ui:widgetContext>
	<script type="text/javascript">
		var widgetId = '<c:out value="${widgetId}" />';

		var pollAddErrorMessage = function(msg) {
			var errdiv = $$('div.msg-error').first();
			errdiv.insert(msg);
			errdiv.show();
		};

		var pollingUpdater = function(request, response) {
			var text = request.responseText;
			if (text != 'NOTHING') {
				pollAddErrorMessage(text);
			}
		};

		var pollingAction = function() {
			// Params:   action(element, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams)
			araneaPage().action(null, "pollrequest", widgetId, null, pollingUpdater);
		};

		var intervalActionId = setInterval(pollingAction, 3000);

		var removePolling = function(event) {
			clearInterval(intervalActionId);
			Event.stopObserving(event.element, 'click', removePolling); 
		}

		$$('a, button, input[type="button"]').each(function(elem) {
			elem.observe('click', removePolling);
		});
	</script>
  </ui:widgetContext>
</jsp:root>
