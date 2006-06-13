<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
  <ui:root>
  	<ui:viewPort>	
			<ui:widgetContext>
			<html>
     		<head>
	        <ui:importScripts includeTemplate="true"/>
	            
	        <title>Aranea Widget Hello Name Example</title>
        </head>			
        
        <body>
        	Hello <c:out value="${contextWidget.data.name}"/>!
				</body>
			</html>				
			</ui:widgetContext>
		</ui:viewPort>
	</ui:root>	
</jsp:root>