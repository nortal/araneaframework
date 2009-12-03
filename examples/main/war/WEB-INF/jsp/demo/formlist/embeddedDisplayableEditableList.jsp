<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" version="2.1">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName>List</tui:componentName>
		</tui:componentHeader>

		<tui:component>
			<tui:componentList>
				<ui:formList id="editableList">
					<ui:formListRows>
						<ui:row>
							<c:choose>
								<c:when test="${formRow.open}">
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
								</c:when>
								<c:otherwise>
									<ui:cell styleClass="center">
										<c:if test="${row.booleanField}">
											<tui:image code="flag" />
										</c:if>
									</ui:cell>
									<ui:cell>
										<c:out value="${row.stringField}" />
									</ui:cell>
									<ui:cell>
										<c:out value="${row.longField}" />
									</ui:cell>
								</c:otherwise>
							</c:choose>				
							<ui:cell width="0">
								<ui:linkButton id="editSave" showLabel="false">
									<tui:image code="buttonChange" />
								</ui:linkButton>

								<c:if test="${formRow.open}">
									<ui:linkButton id="delete" showLabel="false">
										<tui:image code="buttonDelete" />
									</ui:linkButton>
								</c:if>
							</ui:cell>
						</ui:row>
						<ui:row>
							<ui:cell colspan="4">
								<ui:widgetInclude id="editableList.${formRow.rowFormId}.embeddedFormList"/>
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
									<ui:textInput id="stringField" size="40" />
								</ui:cell>
							</ui:formElement>
							<ui:formElement id="longField">
								<ui:cell>
									<ui:numberInput size="5" />
								</ui:cell>
							</ui:formElement>
							<ui:cell width="0">
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
