<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jsp/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="2.1">

    <ui:widgetContext>	        
    	<c:out value="${widget.date}"/><br/>

		Welcome!
		To get started on your own application, you can take the contents under the "war" folder
		and use it as a starting point. Edit the skeleton configuration files as needed, add
		new menu items, write new widget classes. Message shown to you right now comes from 
		"WEB-INF/jsp/blankwidget.jsp" file. Corresponding widget class is "BlankWidget".
    </ui:widgetContext>
</jsp:root>
