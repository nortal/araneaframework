<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.0">
	<ui:widgetContext>

		<script type="text/javascript">
			window.navigate = function(target, event) {
				Aranea.Page.event($$(target).first());
				event.stop();
				return false;
			};
			document.observe('aranea:loaded', function() {
				$$('input[type=text]').first().focus();
			});
		</script>

		<tui:componentHeader>
			<tui:componentName><fmt:message key="simpleEditableList.title"/></tui:componentName>
		</tui:componentHeader>

		<ui:list id="list">
			<ui:formList>
				<tui:component>

					<p><fmt:message key="simpleEditableList.intro"/></p>
					<p><fmt:message key="simpleEditableList.howtonavigate"/></p>

					<tui:componentList>
						<ui:updateRegionRows id="simpleInmemorylistBody">

						<tui:componentListHeader updateRegions="simpleInmemorylistBody,${listId}lfooter"/>

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
									<ui:listFilterButton updateRegions="simpleInmemorylistBody,${listId}lfooter"/>
									<br/>
									<ui:listFilterClearButton updateRegions="simpleInmemorylistBody,${listId}lfooter"/>
								</ui:cell>
							</ui:row>

							<ui:keyboardHandler scope="${formFullId}" keyMetaCond="ctrl+left" handler="navigate.curry('a.prev')"/>
							<ui:keyboardHandler scope="${formFullId}" keyMetaCond="ctrl+right" handler="navigate.curry('a.next')"/>
							<ui:keyboardHandler scope="${formFullId}" keyMetaCond="ctrl+shift+left" handler="navigate.curry('a.first')"/>
							<ui:keyboardHandler scope="${formFullId}" keyMetaCond="ctrl+shift+right" handler="navigate.curry('a.last')"/>
						</ui:listFilter>

						<ui:formListRows>
							<ui:row>
								<c:choose>
									<c:when test="${formRow.open}">
										<ui:cell>
											<ui:textInput id="sex" size="1"/>
										</ui:cell>

										<ui:cell styleClass="right" width="80px">
											<ui:textInput id="forename" styleClass="right"/>
										</ui:cell>

										<ui:cell>
											<ui:textInput id="surname"/>
										</ui:cell>

										<ui:cell>
											<ui:textInput id="country"/>
										</ui:cell>
									</c:when>

									<c:otherwise>
										<ui:cell><c:out value="${row.sex}" /></ui:cell>
										<ui:cell styleClass="right" width="80px"><c:out value="${row.forename}" /></ui:cell>
										<ui:cell><c:out value="${row.surname}" /></ui:cell>
										<ui:cell><c:out value="${row.country}" /></ui:cell>
									</c:otherwise>
								</c:choose>

								<ui:cell width="0">
									<ui:linkButton id="editSave" showLabel="false" updateRegions="simpleInmemorylistBody">
										<tui:image code="buttonChange" alt="common.change"/>
										<fmt:message var="cblbl" key="button.edit"/>
										<c:if test="${not formRow.open}">
											<fmt:message var="cblbl" key="button.edit"/>
											<ui:tooltip element="${formFullId}.editSave" text="${cblbl}"/>
										</c:if>
										<c:if test="${formRow.open}">
											<fmt:message var="cblbl" key="button.save"/>
											<ui:tooltip element="${formFullId}.editSave" text="${cblbl}"/>
										</c:if>
									</ui:linkButton>

									<c:if test="${formRow.open}">
										<ui:linkButton id="delete" showLabel="false" updateRegions="simpleInmemorylistBody,${listId}lfooter">
											<tui:image code="buttonDelete" alt="button.delete"/>
										</ui:linkButton>
										<fmt:message var="rblbl" key="common.remove"/>
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
										<ui:linkButton id="add" showLabel="false" updateRegions="simpleInmemorylistBody,${listId}lfooter">
											<button id="addFormAddbutton"><fmt:message key="button.add"/></button>
										</ui:linkButton>
									</ui:cell>

								</ui:row>
							</ui:formListAddForm>
						</ui:updateRegionRows>
				</tui:componentList>

				<ui:updateRegion id="${listId}lfooter">
					<tui:componentListFooter updateRegions="simpleInmemorylistBody,${listId}lfooter"/>
				</ui:updateRegion>
			</tui:component>

			</ui:formList>
		</ui:list>
	</ui:widgetContext>
</jsp:root>
