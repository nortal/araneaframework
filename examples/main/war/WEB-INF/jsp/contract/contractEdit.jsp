<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template"  version="1.2">		
	<ui:widgetContext>
		
		<h2>Editing contract</h2>
		
		<p>
			Here you can modify data of selected contract.
			Switch through the tabs using the <b>Previous</b> and <b>Next</b> buttons below.
			Press the <b>Submit</b> button when you are finished.
		</p>
		
		<p>
			All fields must be filled.
		</p>
		
		<ui:wizard id="wizard">
			<ui:wizardHeader />
			<ui:wizardBody />
			<ui:wizardFooter />
			<ui:wizardIfPage index="last">
				<p>Last page</p>
			</ui:wizardIfPage>
		</ui:wizard>

	</ui:widgetContext>		
</jsp:root>