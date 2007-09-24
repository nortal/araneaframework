<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName><fmt:message key="sed.title"/></tui:componentName>
		</tui:componentHeader>
		
		<ui:list id="list">
			<ui:formList>
			<tui:component>
			
			<p>
				<fmt:message key="sed.intro"/>
			</p>
			
			<p>
				<fmt:message key="sed.howtonavigate"/>
			</p>
			
			<tui:componentList>
					<tui:componentListHeader/>
					
					<ui:listFilter>
						<ui:row styleClass="filter">
							<ui:cell>
								<ui:textInput id="sex" size="1"/>
							</ui:cell>
	
							<ui:cell width="80px">
								<ui:textInput id="forename"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="surname" size="20"/>
							</ui:cell>
							
							<ui:cell>
								<ui:textInput id="country"/>
							</ui:cell>
							
							<ui:cell>
								<ui:listFilterButton/>
								<br/>
								<ui:listFilterClearButton/>
							</ui:cell>
						</ui:row>

					</ui:listFilter>
				
					<ui:formListRows>
						<ui:row>
							<c:choose>
								<c:when test="${formRow.open}">
									<ui:formElement id="sex">
										<ui:cell>
											<ui:textInput size="1"/>
										</ui:cell>
									</ui:formElement>
									<ui:formElement id="forename">
										<ui:cell styleClass="right" width="80px">
											<ui:textInput styleClass="right"/>
										</ui:cell>
									</ui:formElement>
									<ui:formElement id="surname">
										<ui:cell>
											<ui:textInput />
										</ui:cell>
									</ui:formElement>
									<ui:cell>
										<ui:textInput id="country"/>
									</ui:cell>
								</c:when>
								<c:otherwise>
									<ui:cell>
										<c:out value="${row.sex}" />
									</ui:cell>
									<ui:cell styleClass="right" width="80px">
										<c:out value="${row.forename}" />
									</ui:cell>
									<ui:cell>
										<c:out value="${row.surname}" />
									</ui:cell>
									<ui:cell>
										<c:out value="${row.country}" />
									</ui:cell>
								</c:otherwise>
							</c:choose>
							<ui:cell width="0">
								<ui:linkButton id="editSave" showLabel="false">
									<tui:image code="buttonChange" />
									<fmt:message var="cblbl" key="common.Edit"/>
									<c:if test="${not formRow.open}">
									  <fmt:message var="cblbl" key="common.Edit"/>
									  <ui:tooltip element="${formFullId}.editSave" text="${cblbl}"/>
									</c:if>
									<c:if test="${formRow.open}">
									  <fmt:message var="cblbl" key="button.save"/>
									  <ui:tooltip element="${formFullId}.editSave" text="${cblbl}"/>
									</c:if>
								</ui:linkButton>

								<c:if test="${formRow.open}">
									<ui:linkButton id="delete" showLabel="false">
										<tui:image code="buttonDelete" />
									</ui:linkButton>
									<fmt:message var="rblbl" key="common.Remove"/>
									<ui:tooltip element="${formFullId}.delete" text="${rblbl}"/>
								</c:if>
							</ui:cell>
						</ui:row>
					</ui:formListRows>

					<ui:formListAddForm>
						<ui:row>
								<ui:formElement id="sex">
								<ui:cell>
									<ui:textInput size="1" />
								</ui:cell>
							</ui:formElement>
						
							<ui:cell styleClass="center" width="80px">
								<ui:textInput id="forename" />
							</ui:cell>
							<ui:formElement id="surname">
								<ui:cell>
									<ui:textInput size="20" />
								</ui:cell>
							</ui:formElement>
							
							<ui:formElement id="country">
								<ui:cell>
									<ui:textInput />
								</ui:cell>
							</ui:formElement>

							<ui:cell width="0">
								<ui:linkButton id="add" showLabel="false">
									<button id="addFormAddbutton"><fmt:message key="button.add"/></button>
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
