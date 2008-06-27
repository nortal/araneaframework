<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">
	<ui:systemForm id="overlaySystemForm" method="POST" styleClass="aranea-overlay">
		<div class="msg-info">
			<div>
				<div>
					<ui:messages type="info"/>
				</div>
			</div>
		</div>
		<ui:messages type="error" styleClass="msg-error"/>
		<ui:widgetInclude id="c"/>
	</ui:systemForm>
</jsp:root>
