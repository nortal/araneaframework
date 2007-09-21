<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName>In memory editable List demo</tui:componentName>
		</tui:componentHeader>

		<ui:list id="list">
			<ui:formList>
			<tui:component>
			
			<tui:componentList>

				
					<ui:formListRows>
						<ui:row>
							<c:choose>
								<c:when test="${formRow.open}">
									<ui:formElement id="forename">
										<ui:cell styleClass="right">
											<ui:textInput styleClass="right"/>
										</ui:cell>
									</ui:formElement>
									<ui:formElement id="surname">
										<ui:cell>
											<ui:textInput />
										</ui:cell>
									</ui:formElement>
									<ui:formElement id="sex">
										<ui:cell>
											<ui:textInput/>
										</ui:cell>
									</ui:formElement>
								</c:when>
								<c:otherwise>
									<ui:cell styleClass="right">
										<c:out value="${row.forename}" />
									</ui:cell>
									<ui:cell>
										<c:out value="${row.surname}" />
									</ui:cell>
									<ui:cell>
										<c:out value="${row.sex}" />
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
					</ui:formListRows>

					<ui:formListAddForm>
						<ui:row>
							<ui:cell styleClass="center">
								<ui:textInput id="forename" />
							</ui:cell>
							<ui:formElement id="surname">
								<ui:cell>
									<ui:textInput size="40" />
								</ui:cell>
							</ui:formElement>
							<ui:formElement id="sex">
								<ui:cell>
									<ui:textInput size="5" />
								</ui:cell>
							</ui:formElement>
							<ui:cell width="0">
								<ui:linkButton id="add" showLabel="false">
									<tui:image code="buttonAdd" />
								</ui:linkButton>
							</ui:cell>
						</ui:row>
					</ui:formListAddForm>
				
			</tui:componentList>
			
			<tui:componentListFooter/>
		</tui:component>
		
		</ui:formList>
		</ui:list>
	</ui:widgetContext>
</jsp:root>
