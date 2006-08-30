<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<ui:componentHeader>
			<ui:componentName>List</ui:componentName>
		</ui:componentHeader>

		<ui:component>
			<ui:componentList>
				<ui:formList id="editableList">
					<ui:formListRows>
						<ui:row>
							<c:choose>
								<c:when test="${formRow.open}">
									<ui:cell styleClass="center">
										<ui:checkbox id="booleanField" />
									</ui:cell>
									<ui:cell>
										<ui:textInput id="stringField" />
									</ui:cell>
									<ui:formElement id="longField">
										<ui:cell>
											<ui:numberInput/>
										</ui:cell>
									</ui:formElement>
								</c:when>
								<c:otherwise>
									<ui:cell styleClass="center">
										<c:if test="${row.booleanField}">
											<ui:image code="flag" />
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
									<ui:image code="buttonChange" />
								</ui:linkButton>

								<c:if test="${formRow.open}">
									<ui:linkButton id="delete" showLabel="false">
										<ui:image code="buttonDelete" />
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
							<ui:cell>
								<ui:textInput id="stringField" size="40" />
							</ui:cell>
							<ui:formElement id="longField">
								<ui:cell>
									<ui:numberInput size="5" />
								</ui:cell>
							</ui:formElement>
							<ui:cell width="0">
								<ui:linkButton id="add" showLabel="false">
									<ui:image code="buttonAdd" />
								</ui:linkButton>
							</ui:cell>
						</ui:row>
					</ui:formListAddForm>
				</ui:formList>
			</ui:componentList>
		</ui:component>
	</ui:widgetContext>
</jsp:root>
