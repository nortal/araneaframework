<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.1">
	<ui:widgetContext>

		<tui:componentHeader>
			<tui:componentName><fmt:message key="inMemoryEditableList.title"/></tui:componentName>
		</tui:componentHeader>

		<tui:component>
			<p><fmt:message key="inMemoryEditableList.desc"/></p>

			<tui:componentList>
				<ui:formList id="editableList">

					<!-- The list rows. Note that we don't check here whether the list row is open for editing. -->
					<!-- In this demo, the rows are always open for editing -->
					<ui:formListRows>
						<ui:row>

							<ui:cell styleClass="center" width="10%">
								<ui:checkbox id="booleanField" />
							</ui:cell>

							<ui:cell width="40%">
								<ui:textInput id="stringField" styleClass="w200"/>
							</ui:cell>

							<ui:cell width="40%">
								<ui:numberInput id="longField" styleClass="w200"/>
							</ui:cell>

							<ui:cell width="10%">
								<ui:linkButton id="save" showLabel="false">
									<tui:image code="buttonChange" title="button.save"/>
								</ui:linkButton>
								<ui:linkButton id="delete" showLabel="false">
									<tui:image code="buttonDelete" title="button.delete"/>
								</ui:linkButton>
							</ui:cell>

						</ui:row>
					</ui:formListRows>
	
					<!-- The form for adding new rows to this list: -->
					<ui:formListAddForm>
						<ui:row>

							<ui:cell styleClass="center">
								<ui:checkbox id="booleanField" />
							</ui:cell>

							<ui:cell>
								<ui:textInput id="stringField" styleClass="w200"/>
							</ui:cell>

							<ui:cell>
								<ui:numberInput id="longField" styleClass="w200"/>
							</ui:cell>

							<ui:cell width="10%">
								<ui:linkButton id="add" showLabel="false">
									<tui:image code="add" title="button.add"/>
								</ui:linkButton>
							</ui:cell>

						</ui:row>
					</ui:formListAddForm>

				</ui:formList>
			</tui:componentList>
		</tui:component>

	</ui:widgetContext>
</jsp:root>
