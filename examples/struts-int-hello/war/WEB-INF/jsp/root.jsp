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

				<title>Aranea Struts Integration Example</title>
				</head>

				<ui:body>
					<ui:systemForm id="systemForm" method="GET">
					  <h3>Aranea Hello World</h3>
						<div style="width: 80%; border-color: green; border-style: solid; border-width: 2px; padding: 10px">						
								<ui:widgetInclude id="araneaHelloWorld" />															
						</div>
					
						<br/><br/><br/>
						<h3>Struts Hello World</h3>
						<div style="width: 80%; border-color: red; border-style: solid; border-width: 2px; padding: 10px">
							<ui:widgetInclude id="strutsHelloWorld" />					
						</div>
					</ui:systemForm>	
				</ui:body>
				</html>
			</ui:widgetContext>
		</ui:viewPort>
	</ui:root>
</jsp:root>
