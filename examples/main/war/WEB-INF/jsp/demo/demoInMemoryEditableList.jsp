<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="2.0">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName>In memory editable List demo</tui:componentName>
		</tui:componentHeader>
		<tui:component>

			<tui:componentList>
				<ui:formList id="editableList">
					<ui:formListRows>
						<ui:row>
							<ui:cell styleClass="center">
								<ui:checkbox id="booleanField" />
							</ui:cell>
							<ui:formElement id="stringField">
								<ui:cell>
									<ui:textInput/>
								</ui:cell>
							</ui:formElement>
							<ui:formElement id="longField">
								<ui:cell>
									<ui:numberInput/>
								</ui:cell>
							</ui:formElement>
							<ui:cell width="0">
								<ui:linkButton id="save" showLabel="false">
									<tui:image code="buttonChange" />
								</ui:linkButton>
								<ui:linkButton id="delete" showLabel="false">
									<tui:image code="buttonDelete" />
								</ui:linkButton>
							</ui:cell>
						</ui:row>
					</ui:formListRows>
					<ui:formListAddForm>
						<ui:row>
							<ui:cell styleClass="center">
								<ui:checkbox id="booleanField" />
							</ui:cell>
							<ui:formElement id="stringField">
								<ui:cell>
									<ui:textInput size="40" />
								</ui:cell>
							</ui:formElement>
							<ui:formElement id="longField">
								<ui:cell>
									<ui:numberInput size="5" />
								</ui:cell>
							</ui:formElement>
							<ui:cell width="0">
								<ui:linkButton id="add" showLabel="false">
									<tui:image code="add" />
								</ui:linkButton>
							</ui:cell>
						</ui:row>
					</ui:formListAddForm>
				</ui:formList>
			</tui:componentList>

			<tui:componentActions>
				<ui:eventButton labelId="#Test" eventId="test" />
				<c:if test="${viewData.askCloseConfirmation == 'true'}">
					<ui:onLoadEvent
						event="if (confirm('Do you wish to lose unsaved changes?')) araneaPage().event_6(araneaPage().getSystemForm(), 'close', '${widgetId}', 'true', null, null);"/>
				</c:if>
				<ui:eventButton labelId="#Close" eventId="close" />
			</tui:componentActions>

		</tui:component>
	</ui:widgetContext>
</jsp:root>
