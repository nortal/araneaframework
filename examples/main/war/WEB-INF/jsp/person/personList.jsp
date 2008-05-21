<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2"
>
	<ui:widgetContext>
		<ui:list id="personList">
			<tui:componentHeader>
				<tui:componentName>Persons List</tui:componentName>
			</tui:componentHeader>
				
			<tui:component>

				<tui:componentList width="1000px">
					<!-- Title -->
					<tui:componentListHeader/>

					<!-- Filter -->
					<ui:listFilter>
						<ui:row styleClass="filter">
							<ui:cell>
								<ui:listSelectAllCheckBox/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="name"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="surname"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="phone"/>
							</ui:cell>
							
							<ui:cell>
								<ui:dateInput id="birthdate_start"/>
								<br/>
								<ui:dateInput id="birthdate_end"/> 
							</ui:cell>
							
			                <ui:cell>
			                  <ui:floatInput id="salary_start" styleClass="min"/>
			                  <br/>
			                  <ui:floatInput id="salary_end" styleClass="min"/>
			                </ui:cell>							

							<ui:cell>
								<ui:listFilterButton/>
								<br/>
								<ui:listFilterClearButton/>
							</ui:cell>
						</ui:row>

					</ui:listFilter>
					
					<ui:listRows>
						<ui:row>
							<ui:cell>
								<ui:listRowCheckBox/>
							</ui:cell>
	
							<ui:cell>
								<ui:listRowLinkButton eventId="select">
									<c:out value="${row.name}"/>
								</ui:listRowLinkButton>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.surname}"/>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.phone}"/>
							</ui:cell>
							
							<ui:cell>
								<fmt:formatDate value="${row.birthdate}" pattern="dd.MM.yyyy"/>
							</ui:cell>

							<ui:cell>
								<c:out value="${row.salary}"/>
							</ui:cell>
							
							<ui:cell>
								<ui:listRowLinkButton eventId="edit">
									<tui:image code="buttonChange" alt="Edit person" title="Edit person"/>
								</ui:listRowLinkButton>
								<ui:listRowLinkButton eventId="remove">
									<tui:image code="buttonDelete" alt="Remove person" title="Remove person"/>
								</ui:listRowLinkButton>
							</ui:cell>
							
						</ui:row>
					</ui:listRows>
				</tui:componentList>

				<tui:componentListFooter/>
				
				<tui:componentActions>
					<ui:eventButton eventId="add" labelId="#Add new person"/>
					<ui:nbsp/>
					<ui:eventButton eventId="collect" labelId="#Collect selected persons"/>
				</tui:componentActions>

			</tui:component>
		
		</ui:list>
	</ui:widgetContext>
</jsp:root>