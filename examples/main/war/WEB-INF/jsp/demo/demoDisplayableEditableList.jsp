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
						<ui:newRow>
							<c:choose>
								<c:when test="${formRow.open}">
									<ui:newCell styleClass="center">
										<ui:checkbox id="booleanField" />
									</ui:newCell>
									<ui:newCell>
										<ui:textInput id="stringField" />
									</ui:newCell>
									<ui:newCell>
										<ui:numberInput id="longField" />
									</ui:newCell>
								</c:when>
								<c:otherwise>
									<ui:newCell styleClass="center">
										<c:if test="${row.booleanField}">
											<ui:image code="flag" />
										</c:if>
									</ui:newCell>
									<ui:newCell>
										<c:out value="${row.stringField}" />
									</ui:newCell>
									<ui:newCell>
										<c:out value="${row.longField}" />
									</ui:newCell>
								</c:otherwise>
							</c:choose>
							<ui:newCell width="0">
								<ui:linkButton id="editSave" showLabel="false">
									<ui:image code="buttonChange" />
								</ui:linkButton>

								<c:if test="${formRow.open}">
									<ui:linkButton id="delete" showLabel="false">
										<ui:image code="buttonDelete" />
									</ui:linkButton>
								</c:if>
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
									<ui:image code="buttonAdd" />
								</ui:linkButton>
							</ui:newCell>
						</ui:newRow>
					</ui:formListAddForm>
				</ui:formList>
			</ui:componentList>
		</ui:component>
	</ui:widgetContext>
</jsp:root>
