<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>GWT Dynamic Table</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<p>
				<ui:eventButton eventId="test" labelId="#Empty submit"/>
			</p>
			<p>
				<ui:eventButton eventId="start" labelId="#Start new flow"/>
			</p>

			<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"><jsp:text> </jsp:text></iframe>

			<ui:gwtWidgetInclude id="dynaTable" styleClass="DynaTable">
				<h1>School Schedule for Professors and Students</h1>
				<table width="100%" border="0" summary="School Schedule for Professors and Students">
					<tr valign="top">
						<td id="calendar" align="center" width="90%">
						</td>
						<td id="days" align="center" width="10%">
						</td>
					</tr>
				</table>
			</ui:gwtWidgetInclude>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
