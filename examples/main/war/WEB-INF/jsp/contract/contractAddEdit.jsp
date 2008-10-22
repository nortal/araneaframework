<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"  version="2.0">		
	<ui:widgetContext>
		
		<h2>Adding new contract</h2>
		
		<p>
			Here you can fill the data of new contract.
			Switch through the tabs using the <b>Previous</b> and <b>Next</b> buttons below.
			Press the <b>Submit</b> button when you are finished.
		</p>
		
		<p>
			All fields must be filled.
		</p>
		
		<tui:wizard id="wizard">
			<tui:wizardHeader />
			<tui:wizardBody />
			<tui:wizardFooter />
		</tui:wizard>

	</ui:widgetContext>
</jsp:root>