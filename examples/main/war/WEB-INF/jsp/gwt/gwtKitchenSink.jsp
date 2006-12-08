<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>GWT Kitchen Sink</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<ui:eventButton eventId="test" labelId="#Empty submit"/>

			<!-- <iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe> -->

			<ui:gwtWidgetInclude id="kitchen1"/>
			<ui:gwtWidgetInclude id="kitchen2"/>
			<ui:gwtWidgetInclude id="kitchen3"/>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
