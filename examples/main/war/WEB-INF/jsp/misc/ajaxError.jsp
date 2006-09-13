<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/template"  version="1.2">
	
	When the button is clicked, failing ajax request is made. 
	<ui:updateRegion id="ajaxErrorDemo">
		<ui:newLine/>
		<ui:eventLinkButton labelId="#Error" eventId="error" updateRegions="ajaxErrorDemo"/>
	</ui:updateRegion>
</jsp:root>
