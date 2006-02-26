<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">		
	<h2>Home</h2>
		
	<p>
		This template application demonstrates you the basics of Aranea framework.<ui:newLine/>
		It includes <code>StandardCallStackWidget</code> and <code>StandardWizardWidget</code> as flow containers.<ui:newLine/>
		The first is used to show and make pages (Widgets) interact with each other in different order.<ui:newLine/>
		The latter is used to composite many pages as one and show them as wizard tabs.<ui:newLine/>
		This application also demonstrates the use of <code>FormWidget</code> and <code>ListWidget</code><ui:newLine/> as common user interface elements.
	</p>
	
	<p>
		In this application, you can manage a set of person and companies as well as contracts between them.<ui:newLine/>
		With any of them you can <b>select</b> one from the list and retrieves it's Id,<ui:newLine/>
		<b>view</b> or <b>edit</b> it's data or <b>add</b> a new element. If you select an item from the list,<ui:newLine/>
		same Widget is used to display the list dispite the event following it. You can also access the<ui:newLine/>
		<b>add</b> function from here or when you see a list of elements in order to edit one.<ui:newLine/>
		When adding or editing a contract you have to attach a person as well as a company with it -<ui:newLine/>
		again same Widgets are used to list the elemetns. When you view a contract, you see the names of person<ui:newLine/>
		and company attached to it, but you can also follow the links to see the information from the<ui:newLine/>
		compoments' own view pages.<ui:newLine/>
		The order of pages is dynamic and you can always return back to the page you came.
	</p>
</jsp:root>