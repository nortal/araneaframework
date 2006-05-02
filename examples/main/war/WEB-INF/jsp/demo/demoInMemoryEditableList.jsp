<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<ui:component>
			<ui:componentHeader>
				<ui:componentName>In memory editable List demo</ui:componentName>
			</ui:componentHeader>

			<ui:componentList>
				<ui:formList id="editableList">
					<ui:formListRows>
						<ui:newRow>
							<ui:newCell styleClass="center">
								<ui:checkbox id="booleanField" />
							</ui:newCell>
							<ui:newCell>
								<ui:textInput id="stringField" />
							</ui:newCell>
							<ui:newCell>
								<ui:numberInput id="longField" />
							</ui:newCell>
							<ui:newCell width="0">
								<ui:linkButton id="save" showLabel="false">
									<ui:image code="buttonChange" />
								</ui:linkButton>
								<ui:linkButton id="delete" showLabel="false">
									<ui:image code="buttonDelete" />
								</ui:linkButton>
							</ui:newCell>
						</ui:newRow>
					</ui:formListRows>
					<ui:formListAddForm>
						<ui:newRow>
							<ui:newCell styleClass="center">
								<ui:checkbox id="booleanField" />
							</ui:newCell>
							<ui:newCell>
								<ui:textInput id="stringField" size="40" />
							</ui:newCell>
							<ui:newCell>
								<ui:numberInput id="longField" size="5" />
							</ui:newCell>
							<ui:newCell width="0">
								<ui:linkButton id="add" showLabel="false">
									<ui:image code="add" />
								</ui:linkButton>
							</ui:newCell>
						</ui:newRow>
					</ui:formListAddForm>
				</ui:formList>
			</ui:componentList>

			<ui:componentActions>
				<ui:eventButton labelId="#Test" eventId="test" />
				<c:if test="${contextWidget.data.askCloseConfirmation == 'true'}">
					<ui:onLoadEvent
						event="if (confirm('Do you wish to lose unsaved changes?')) araneaStandardSubmitEvent(${systemFormId}, '${contextWidgetId}', 'close', 'true');" />
				</c:if>
				<ui:eventButton labelId="#Close" eventId="close" />
			</ui:componentActions>

		</ui:component>
	</ui:widgetContext>
</jsp:root>
