<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jstl/core"
  xmlns:fmt="http://java.sun.com/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
  version="1.2">

  <ui:widgetContext>
    <div id="pollingdiv">
       <p>Event calendar</p>
       
       <table id="ptable">
       	<tr><td/><td/></tr>
       </table>
    </div>

    <script type="text/javascript">
        var widgetId = '<c:out value="${widgetId}" />';
    	var pollingUpdater = function(request, response) {
    	  var text = request.responseText;
    	  new Insertion.Bottom($('ptable'), text);
        };

		var pollingAction = function() {
		  araneaPage().action($('pollingdiv'), "pollrequest", widgetId, null, pollingUpdater); 
		};

		setInterval(pollingAction, 3000);
    </script>
  </ui:widgetContext>
</jsp:root>
