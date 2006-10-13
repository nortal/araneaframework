<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	version="1.2">
	<ui:root>
		<ui:viewPort>
			<ui:widgetContext>
				<html>
					<head>
						<ui:importScripts />
						<title>Aranea Widget Hello Name Example</title>
					</head>
	
					<ui:body>
						<ui:systemForm method="GET">
							<ui:widgetInclude id="flowContainer1" /><br/><br/>
							<ui:widgetInclude id="flowContainer2" /><br/><br/>
							<ui:widgetInclude id="flowContainer3" /><br/><br/>
						</ui:systemForm>
					</ui:body>
				</html>
			</ui:widgetContext>
		</ui:viewPort>
	</ui:root>
</jsp:root>
