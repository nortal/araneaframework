<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">

	<script language="JavaScript1.2" type="text/javascript">
		addSystemLoadEvent(processPopups);
		<c:forEach var="popup" items="${outputData.attributes['popupWindows']}">
    			addPopup('<c:out value="${popup.key}"/>', '<c:out value="${popup.value}"/>');
		</c:forEach>
    </script>
</jsp:root>