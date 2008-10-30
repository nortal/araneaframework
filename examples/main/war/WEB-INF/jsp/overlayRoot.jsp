<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">
	<ui:systemForm id="overlaySystemForm" method="POST" styleClass="aranea-overlay">
		<ui:messages type="error" styleClass="msg-error"/>
		<ui:messages type="warning" styleClass="msg-warning"/>
		<ui:messages type="info" styleClass="msg-info"/>
		<ui:widgetInclude id="c"/>
	</ui:systemForm>
</jsp:root>
