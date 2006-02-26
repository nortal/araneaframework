<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">

	<ui:widgetContext>
	
		<h2>Home</h2>
		
		<p>
			This template application demonstrates you the basics of Aranea framework.<ui:newLine/>
			It includes <code>StandardCallStackWidget</code> and <code>StandardWizardWidget</code> as flow containers.			
			The first is used to show and make pages (Widgets) interact with each other in different order.
			The latter is used to composite many pages as one and show them as wizard tabs.<ui:newLine/>
			This application also demonstrates the use of <code>FormWidget</code> and <code>ListWidget</code> as common user interface elements.
		</p>
		
		<p>
			In this application, you can manage a set of person and companies as well as contracts between them.
			With any of them you can <b>select</b> one from the list and retrieves it's Id,
			<b>view</b> or <b>edit</b> it's data or <b>add</b> a new element. If you select an item from the list,
			same Widget is used to display the list dispite the event following it. You can also access the
			<b>add</b> function from here or when you see a list of elements in order to edit one.<ui:newLine/>
			When adding or editing a contract you have to attach a person as well as a company with it -
			again same Widgets are used to list the elemetns. When you view a contract, you see the names of person
			and company attached to it, but you can also follow the links to see the information from the
			compoments' own view pages.<ui:newLine/>
			The order of pages is dynamic and you can always return back to the page you came.
		</p>
	
		<p>
			<jsp:text>Persons: </jsp:text>
			<ui:eventButton styleClass="test-button" eventId="selectPerson" labelId="#Select"/>
			<ui:eventButton styleClass="test-button" eventId="viewPerson" labelId="#View"/>
			<ui:eventButton styleClass="test-button" eventId="addPerson" labelId="#Add"/>
			<ui:eventButton styleClass="test-button" eventId="editPerson" labelId="#Edit"/>
		</p>
	
		<p>
			<jsp:text>Companies: </jsp:text>
			<ui:eventButton eventId="selectCompany" labelId="#Select"/>
			<ui:eventButton eventId="viewCompany" labelId="#View"/>
			<ui:eventButton eventId="addCompany" labelId="#Add"/>
			<ui:eventButton eventId="editCompany" labelId="#Edit"/>
		</p>		
	
		<p>
			<jsp:text>Contracts: </jsp:text>
			<ui:eventButton eventId="selectContract" labelId="#Select"/>
			<ui:eventButton eventId="viewContract" labelId="#View"/>
			<ui:eventButton eventId="addContract" labelId="#Add"/>
			<ui:eventButton eventId="editContract" labelId="#Edit"/>
		</p>
		
		<p>
			<jsp:text>SampleApp stuff: </jsp:text>
			<ui:eventButton eventId="sampleSimpleForm" labelId="#Simple Form"/>
			<ui:eventButton eventId="sampleSimpleList" labelId="#Simple List"/>
			<ui:eventButton eventId="sampleSearchForm" labelId="#Search Form"/>
		</p>
		
		<p>
			<jsp:text>Demo stuff: </jsp:text>
			<ui:eventButton eventId="displayForm" labelId="#Display Form"/>
			<ui:eventButton eventId="editableList" labelId="#Editable List"/>
			<ui:eventButton eventId="inMemoryEditableList" labelId="#In memory editable List"/>
			<ui:eventButton eventId="editableCheckBoxList" labelId="#Editable checkbox List"/>
		</p>
		
		<p>
			<jsp:text>Lists Refactoring: </jsp:text>
			<ui:eventButton eventId="selectPersonRefactor" labelId="#Persons (HSql)"/>
			<ui:eventButton eventId="selectPinnaveehaare" labelId="#Pinnaveehaare (Oracle)"/>
		</p>		

		<p>	
			<jsp:text>Return value: </jsp:text>
			<c:out value="${contextWidget.data.returnValue}"/>
		</p>
	
	</ui:widgetContext>	
</jsp:root>