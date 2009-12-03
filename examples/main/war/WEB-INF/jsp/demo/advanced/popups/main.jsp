<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.1">

  <ui:widgetContext>
  	<script type="text/javascript">
		window['tehcallback'] = function(request, response) {
			if (request.status != 200) {
				alert(request.responseText);	// Very ugly
				return;
			}
			eval(request.responseText);
		};
	</script>
  
 	<ui:widgetInclude id="editableList"/>
 	<ui:widgetInclude id="anotherEditableList"/>
 	<ui:widgetInclude id="yanotherEditableList"/>
  </ui:widgetContext>
</jsp:root>
